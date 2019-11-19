package com.topcoder.scraper.module.ecisolatedmodule.amazon;

import com.topcoder.common.config.CheckItemsDefinitionProperty;
import com.topcoder.common.config.MonitorTargetDefinitionProperty;
import com.topcoder.common.repository.CheckResultRepository;
import com.topcoder.common.repository.ECSiteAccountRepository;
import com.topcoder.common.repository.NormalDataRepository;
import com.topcoder.scraper.module.ecisolatedmodule.AbstractChangeDetectionCheckModule;
import com.topcoder.scraper.service.WebpageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Amazon implementation of ChangeDetectionCheckModule
 */
@Component
public class AmazonChangeDetectionCheckModule extends AbstractChangeDetectionCheckModule {

  private static final Logger LOGGER = LoggerFactory.getLogger(AmazonChangeDetectionCheckModule.class);

  @Autowired
  public AmazonChangeDetectionCheckModule(
          MonitorTargetDefinitionProperty  monitorTargetDefinitionProperty,
          WebpageService                   webpageService,
          ECSiteAccountRepository          ecSiteAccountRepository,
          NormalDataRepository             normalDataRepository,
          AmazonPurchaseHistoryListModule  purchaseHistoryListModule,
          AmazonProductDetailModule        productDetailModule,
          CheckItemsDefinitionProperty     checkItemsDefinitionProperty,
          CheckResultRepository            checkResultRepository

  ) {
    super(
            monitorTargetDefinitionProperty,
            webpageService,
            ecSiteAccountRepository,
            normalDataRepository,
            purchaseHistoryListModule,
            productDetailModule,
            checkItemsDefinitionProperty,
            checkResultRepository
    );
  }

  @Override
  public String getModuleType() {
    return "amazon";
  }
}