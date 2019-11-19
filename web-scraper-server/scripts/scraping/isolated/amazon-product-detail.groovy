// Configurations
class Vars {
    static def productUrlBase =
            "https://www.amazon.co.jp/gp/product/"
            //"https://www.amazon.com/gp/product/"
    static def prices = [
            "#priceblock_ourprice",
            "#MediaMatrix > div > div > ul > li.selected > span > span.a-button-selected > span > a > span > span.a-color-price"
    ]
    static def name =
            "#productTitle"
    static def salesRank =
            "#SalesRank"
    static def productInfoTable =
            "#productDetails_detailBullets_sections1"

    // model-no experimanet 1
    static def modelNoLabelNames = [
            "製品型番",
            "製品型番",
            "製品型番",
            "メーカー型番",
            "メーカー型番",
            "モデル",
            "製造元リファレンス"
    ]
    static def modelNoLabelSelectors = [
            "div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(8) > td.label",
            "#prodDetails > div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(2) > td.label",
            "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(3) > b",
            "#detail_bullets_id > table > tbody > tr > td > div > ul > li:nth-child(2) > b",
            "#detail_bullets_id > table > tbody > tr > td > div > ul > li:nth-child(3) > b",
            "#prodDetails > div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(2) > td.label",
            "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(2) > b"
    ]
    static def modelNoValueSelectors = [
            "div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(8) > td.value",
            "#prodDetails > div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(2) > td.value",
            "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(3)",
            "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(2)",
            "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(3)",
            "#prodDetails > div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(2) > td.value",
            "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(2)"
    ]

    // model-no experimanet 2
    static def modelNoSelectors = [
            [label_name    : "製品型番",
             label_selector: "div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(8) > td.label",
             value_selector: "div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(8) > td.value"
            ],
            [label_name    : "製品型番",
             label_selector: "#prodDetails > div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(2) > td.label",
             value_selector: "#prodDetails > div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(2) > td.value"
            ],
            [label_name    : "製品型番",
             label_selector: "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(3) > b",
             value_selector: "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(3)"
            ],
            [label_name    : "メーカー型番",
             label_selector: "#detail_bullets_id > table > tbody > tr > td > div > ul > li:nth-child(2) > b",
             value_selector: "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(2)"
            ],
            [label_name    : "メーカー型番",
             label_selector: "#detail_bullets_id > table > tbody > tr > td > div > ul > li:nth-child(3) > b",
             value_selector: "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(3)"
            ],
            [label_name    : "モデル",
             label_selector: "#prodDetails > div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(2) > td.label",
             value_selector: "#prodDetails > div.wrapper.JPlocale > div.column.col1 > div > div.content.pdClearfix > div > div > table > tbody > tr:nth-child(2) > td.value"
            ],
            [label_name    : "製造元リファレンス",
             label_selector: "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(2) > b",
             value_selector: "#detail_bullets_id > table > tbody > tr > td > div.content > ul > li:nth-child(2)"
            ]
    ]
}

// Script Main
log("--------------script begin--------------")

productUrl = Vars.productUrlBase + productCode;

setPage(productUrl)
savePage("product-detail")

scrapePrices(Vars.prices)
scrapeName(Vars.name)

// model-no experimanet 1
//scrapeModelNo(Vars.modelNoLabelNames, Vars.modelNoLabelSelectors, Vars.modelNoValueSelectors)
// model-no experimanet 2
scrapeModelNo(Vars.modelNoSelectors)

categoryInfoList = scrapeCategoryInfoListBySalesRank(Vars.salesRank) { props ->
    props.put("1st_line_regex", "(.*?) - (.*?)位")
    props.put("rest_ranks_selector", "ul > li > span:nth-of-type(1)")
    props.put("rest_paths_selector", "ul > li > span:nth-of-type(2)")
}
if (categoryInfoList.size() <= 0) {
    categoryInfoList = scrapeCategoryInfoListByProductInfoTable(Vars.productInfoTable, { props ->
        props.put("lines_selector", "tbody > tr")
        props.put("ranks_selector", "td > span > span")
    }, { tr_node ->
        //Boolean.valueOf(getTextContent(tr_node.querySelector("th")).contains("Rank"))
        getTextContent(tr_node.querySelector("th")).contains("Rank")
    })
}
scrapeCategoryRanking(categoryInfoList)

log("--------------script end--------------")