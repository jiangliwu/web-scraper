package com.topcoder.api.entity;

import com.gargoylesoftware.htmlunit.WebClient;
import com.topcoder.scraper.config.AmazonProperty;
import com.topcoder.scraper.module.amazon.crawler.AmazonAuthenticationCrawler;
import com.topcoder.scraper.service.WebpageService;
import lombok.Data;

/**
 * the Crawler context for each session/uuid
 */
@Data
public class CrawlerContext {

  /**
   * the web client
   */
  private WebClient webClient;

  /**
   * the Amazon Property
   */
  private AmazonProperty property;

  /**
   * the web page service
   */
  private WebpageService webpageService;

  /**
   * the session/task id
   */
  private String uuid;

  /**
   * the auth crawler
   */
  private AmazonAuthenticationCrawler crawler;


  public CrawlerContext(WebClient webClient, AmazonProperty property, WebpageService webpageService, String uuid,
                        AmazonAuthenticationCrawler crawler) {
    this.webClient = webClient;
    this.property = property;
    this.webpageService = webpageService;
    this.uuid = uuid;
    this.crawler = crawler;
  }
}
