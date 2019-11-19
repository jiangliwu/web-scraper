package com.topcoder.scraper.module.ecisolatedmodule;

import com.topcoder.common.config.MonitorTargetDefinitionProperty;
import com.topcoder.common.dao.ECSiteAccountDAO;
import com.topcoder.common.repository.ECSiteAccountRepository;
import com.topcoder.common.repository.NormalDataRepository;
import com.topcoder.scraper.Consts;
import com.topcoder.scraper.module.IBasicModule;
import com.topcoder.scraper.module.ecisolatedmodule.crawler.AbstractProductDetailCrawlerResult;
import com.topcoder.scraper.module.ecisolatedmodule.crawler.AbstractPurchaseHistoryListCrawlerResult;
import com.topcoder.scraper.service.WebpageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class for ChangeDetectionInitModule
 */
public abstract class AbstractChangeDetectionCommonModule implements IBasicModule {

  private static Logger LOGGER = LoggerFactory.getLogger(AbstractChangeDetectionCommonModule.class);

  protected final MonitorTargetDefinitionProperty    monitorTargetDefinitionProperty;
  protected final WebpageService                     webpageService;
  protected final ECSiteAccountRepository            ecSiteAccountRepository;
  protected final NormalDataRepository               normalDataRepository;
  protected final AbstractPurchaseHistoryListModule  purchaseHistoryListModule;
  protected final AbstractProductDetailModule        productDetailModule;

  public AbstractChangeDetectionCommonModule(
          MonitorTargetDefinitionProperty    monitorTargetDefinitionProperty,
          WebpageService                     webpageService,
          ECSiteAccountRepository            ecSiteAccountRepository,
          NormalDataRepository               normalDataRepository,
          AbstractPurchaseHistoryListModule  purchaseHistoryListModule,
          AbstractProductDetailModule        productDetailModule
  ) {
    this.monitorTargetDefinitionProperty = monitorTargetDefinitionProperty;
    this.webpageService                  = webpageService;
    this.ecSiteAccountRepository         = ecSiteAccountRepository;
    this.normalDataRepository            = normalDataRepository;
    this.purchaseHistoryListModule       = purchaseHistoryListModule;
    this.productDetailModule             = productDetailModule;
  }

  abstract public String getModuleType();

  /**
   * Implementation of check method
   */
  protected void processMonitorTarget() throws IOException {
    LOGGER.info("[processMonitorTarget] in");
    for (MonitorTargetDefinitionProperty.MonitorTargetCheckSite checkSite : monitorTargetDefinitionProperty.getCheckSites()) {
      if (!this.getModuleType().equalsIgnoreCase(checkSite.getEcSite())) {
        continue;
      }

      for (MonitorTargetDefinitionProperty.MonitorTargetCheckPage monitorTargetCheckPage : checkSite.getCheckPages()) {

        if (monitorTargetCheckPage.getPageName().equalsIgnoreCase(Consts.PURCHASE_HISTORY_LIST_PAGE_NAME)) {
          LOGGER.info("[processMonitorTarget] processPurchaseHistory for target accounts");

          List<Integer> userIdList = monitorTargetCheckPage.getCheckTargetKeys()
                  .stream().map(e -> Integer.valueOf(e)).collect(Collectors.toList());
          LOGGER.info("[processMonitorTarget] account ids: " + userIdList);
          Iterable<ECSiteAccountDAO> accountDAOS = ecSiteAccountRepository.findAllByEcSiteAndUserIdIn(this.getModuleType(), userIdList);

          for (ECSiteAccountDAO ecSiteAccountDAO : accountDAOS) {
            AbstractPurchaseHistoryListCrawlerResult crawlerResult =
                    this.purchaseHistoryListModule.fetchPurchaseHistoryListForECSiteAccount(ecSiteAccountDAO, null);
            if (crawlerResult != null) {
              String key = Integer.toString(ecSiteAccountDAO.getId());
              this.processPurchaseHistory(crawlerResult, key);
            }
          }

          // process puchase history
        } else if (monitorTargetCheckPage.getPageName().equalsIgnoreCase(Consts.PRODUCT_DETAIL_PAGE_NAME)) {
          LOGGER.info("[processMonitorTarget] processProductDetail for target products");

          // TODO: fix below hacky code, monitorTargetCheckPage.getCheckTargetKeys() must not be null.
          if (monitorTargetCheckPage.getCheckTargetKeys() == null) continue;

          for (String productCode : monitorTargetCheckPage.getCheckTargetKeys()) {
            AbstractProductDetailCrawlerResult crawlerResult =
                    this.productDetailModule.fetchProductDetail(productCode);
            if (crawlerResult != null) {
              this.processProductInfo(crawlerResult);
            }
          }

        } else {
          throw new RuntimeException("[processMonitorTarget] Unknown monitor target definition " + monitorTargetCheckPage.getPageName());
        }

      }
    }

  }

  /**
   * Process purchase history crawler result
   * @param crawlerResult the crawler result
   * @param pageKey the page key
   */
  abstract protected void processPurchaseHistory(AbstractPurchaseHistoryListCrawlerResult crawlerResult, String pageKey);

  /**
   * Process product info crawler result
   * @param crawlerResult the crawler result
   */
  abstract protected void processProductInfo(AbstractProductDetailCrawlerResult crawlerResult);
}