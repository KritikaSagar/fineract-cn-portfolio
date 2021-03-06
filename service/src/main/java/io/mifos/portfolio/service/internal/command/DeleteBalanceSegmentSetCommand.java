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
package io.mifos.portfolio.service.internal.command;

/**
 * @author Myrle Krantz
 */
public class DeleteBalanceSegmentSetCommand {
  private final String productIdentifier;
  private final String balanceSegmentSetIdentifier;

  public DeleteBalanceSegmentSetCommand(String productIdentifier, String balanceSegmentSetIdentifier) {
    this.productIdentifier = productIdentifier;
    this.balanceSegmentSetIdentifier = balanceSegmentSetIdentifier;
  }

  public String getProductIdentifier() {
    return productIdentifier;
  }

  public String getBalanceSegmentSetIdentifier() {
    return balanceSegmentSetIdentifier;
  }

  @Override
  public String toString() {
    return "DeleteBalanceSegmentSetCommand{" +
        "productIdentifier='" + productIdentifier + '\'' +
        ", balanceSegmentSetIdentifier='" + balanceSegmentSetIdentifier + '\'' +
        '}';
  }
}
