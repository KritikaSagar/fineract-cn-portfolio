/*
 * Copyright 2017 Kuelap, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mifos.portfolio.service.internal.checker;

import io.mifos.core.lang.ServiceException;
import io.mifos.portfolio.api.v1.domain.Case;
import io.mifos.portfolio.service.internal.pattern.PatternFactoryRegistry;
import io.mifos.portfolio.service.internal.repository.ProductEntity;
import io.mifos.portfolio.service.internal.repository.ProductRepository;
import io.mifos.portfolio.service.internal.service.CaseService;
import io.mifos.portfolio.service.internal.service.ProductService;
import io.mifos.products.spi.PatternFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Myrle Krantz
 */
@Component
public class CaseChecker {
  private final CaseService caseService;
  private final ProductService productService;
  private final ProductRepository productRepository;
  private final PatternFactoryRegistry patternFactoryRegistry;

  @Autowired
  public CaseChecker(final CaseService caseService,
                     final ProductService productService,
                     final ProductRepository productRepository,
                     final PatternFactoryRegistry patternFactoryRegistry) {
    this.caseService = caseService;
    this.productService = productService;
    this.productRepository = productRepository;
    this.patternFactoryRegistry = patternFactoryRegistry;
  }

  public void checkForCreate(final String productIdentifier, final Case instance) {
    caseService.findByIdentifier(productIdentifier, instance.getIdentifier())
        .ifPresent(x -> {throw ServiceException.conflict("Duplicate identifier: " + productIdentifier + "." + x.getIdentifier());});

    final Optional<Boolean> productEnabled = productService.findEnabledByIdentifier(productIdentifier);
    if (!productEnabled.orElseThrow(() -> ServiceException.internalError("Product should exist, but doesn't"))) {
      throw ServiceException.badRequest("Product must be enabled before cases for it can be created: " + productIdentifier);}

    getPatternFactory(productIdentifier).checkParameters(instance.getParameters());
  }

  public void checkForChange(final String productIdentifier, final Case instance) {
    getPatternFactory(productIdentifier).checkParameters(instance.getParameters());
  }

  private PatternFactory getPatternFactory(final String productIdentifier) {
    return productRepository.findByIdentifier(productIdentifier)
        .map(ProductEntity::getPatternPackage)
        .flatMap(patternFactoryRegistry::getPatternFactoryForPackage)
        .orElseThrow(() -> new IllegalArgumentException("Case references unsupported product type."));
  }
}