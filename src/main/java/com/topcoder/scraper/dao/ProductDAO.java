package com.topcoder.scraper.dao;

import com.topcoder.scraper.converter.JpaConverterPurchaseInfoJson;
import com.topcoder.scraper.model.ProductInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "product")
public class ProductDAO {

  /**
   * Product id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**
   * EC Site name
   */
  @Column(name = "ec_site", length = 32)
  private String ecSite;

  /**
   * Product code
   */
  @Column(name = "product_code")
  private String productCode;

  /**
   * Product name
   */
  @Column(name = "product_name")
  private String productName;

  /**
   * Unit price
   */
  @Column(name = "unit_price")
  private String unitPrice;

  /**
   * Product distributor
   */
  @Column(name = "product_distributor")
  private String productDistributor;

  /**
   * Product info as json
   */
  @Column(name = "product_info", columnDefinition = "json")
  @Convert(converter = JpaConverterPurchaseInfoJson.class)
  private ProductInfo productInfo;

  /**
   * Product distributor
   */
  @Column(name = "fetch_info_status")
  private String fetchInfoStatus;

  /**
   * Update at
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_at")
  private Date updateAt;

  @OneToMany(
    mappedBy = "product",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<RankingDAO> rankings = new ArrayList<>();

  public ProductDAO(String site, ProductInfo productInfo) {
    this.ecSite = site;
    this.productCode = productInfo.getCode();
    this.productName = productInfo.getName();
    this.unitPrice = productInfo.getPrice();
    this.productDistributor = productInfo.getDistributor();
    this.productInfo = new ProductInfo(productInfo.getCode(), productInfo.getName(), productInfo.getPrice(), null, productInfo.getDistributor());
    this.updateAt = new Date();
  }

  public ProductDAO(String ecSite, String productCode, String productName, String unitPrice, String productDistributor, ProductInfo productInfo, String fetchInfoStatus, Date updateAt) {
    this.ecSite = ecSite;
    this.productCode = productCode;
    this.productName = productName;
    this.unitPrice = unitPrice;
    this.productDistributor = productDistributor;
    this.productInfo = productInfo;
    this.fetchInfoStatus = fetchInfoStatus;
    this.updateAt = updateAt;
  }

  public ProductDAO() {
  }

  public void addCategory(CategoryDAO category, int rank) {
    Optional<RankingDAO> rankingDao = this.getRankings()
      .stream()
      .filter(r -> r.getId().equals(new RankingDAO.ProductCategoryId(this.getId(), category.getId())))
      .findFirst();

    if (rankingDao.isPresent()) {
      rankingDao.get().setUpdateAt(new Date());
      rankingDao.get().setRanking(rank);
    } else {
      RankingDAO ranking = new RankingDAO(this, category, rank, new Date());
      rankings.add(ranking);
    }
  }

  public void removeCategory(CategoryDAO category) {
    for (Iterator<RankingDAO> iterator = rankings.iterator(); iterator.hasNext(); ) {
      RankingDAO ranking = iterator.next();

      if (ranking.getProduct().equals(this) && ranking.getCategory().equals(category)) {
        iterator.remove();
        ranking.setProduct(null);
        ranking.setCategory(null);
      }
    }
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setEcSite(String ecSite) {
    this.ecSite = ecSite;
  }

  public void setUpdateAt(Date updateAt) {
    this.updateAt = updateAt;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public void setUnitPrice(String unitPrice) {
    this.unitPrice = unitPrice;
  }

  public void setProductDistributor(String productDistributor) {
    this.productDistributor = productDistributor;
  }

  public void setProductInfo(ProductInfo productInfo) {
    this.productInfo = productInfo;
  }

  public void setFetchInfoStatus(String fetchInfoStatus) {
    this.fetchInfoStatus = fetchInfoStatus;
  }

  public void setRankings(List<RankingDAO> rankings) {
    this.rankings = rankings;
  }

  public int getId() {
    return id;
  }

  public String getEcSite() {
    return ecSite;
  }

  public Date getUpdateAt() {
    return updateAt;
  }

  public String getProductCode() {
    return productCode;
  }

  public String getProductName() {
    return productName;
  }

  public String getUnitPrice() {
    return unitPrice;
  }

  public String getProductDistributor() {
    return productDistributor;
  }

  public ProductInfo getProductInfo() {
    return productInfo;
  }

  public String getFetchInfoStatus() {
    return fetchInfoStatus;
  }

  public List<RankingDAO> getRankings() {
    return rankings;
  }
}