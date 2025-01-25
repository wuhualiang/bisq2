/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.desktop.main.content.settings.trade;

import bisq.common.observable.Pin;
import bisq.desktop.ServiceProvider;
import bisq.desktop.common.observable.FxBindings;
import bisq.desktop.common.view.Controller;
import bisq.settings.SettingsService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TradeSettingsController implements Controller {
    @Getter
    private final TradeSettingsView view;
    private final TradeSettingsModel model;
    private final SettingsService settingsService;

    private Pin closeMyOfferWhenTakenPin, maxTradePriceDeviationPin, tradeAgeForRedactingDataPin;

    public TradeSettingsController(ServiceProvider serviceProvider) {
        settingsService = serviceProvider.getSettingsService();
        model = new TradeSettingsModel();
        view = new TradeSettingsView(model, this);
    }

    @Override
    public void onActivate() {
        closeMyOfferWhenTakenPin = FxBindings.bindBiDir(model.getCloseMyOfferWhenTaken())
                .to(settingsService.getCloseMyOfferWhenTaken());
        maxTradePriceDeviationPin = FxBindings.bindBiDir(model.getMaxTradePriceDeviation())
                .to(settingsService.getMaxTradePriceDeviation());
        tradeAgeForRedactingDataPin = FxBindings.bindBiDir(model.getNumDaysAfterRedactingTradeData())
                .to(settingsService.getNumDaysAfterRedactingTradeData());
    }

    @Override
    public void onDeactivate() {
        if (model.getMaxTradePriceDeviationValidator().getHasErrors()) {
            settingsService.getMaxTradePriceDeviation().set(SettingsService.DEFAULT_MAX_TRADE_PRICE_DEVIATION);
        }
        if (model.getNumDaysAfterRedactingTradeDataValidator().getHasErrors()) {
            settingsService.getNumDaysAfterRedactingTradeData().set(SettingsService.DEFAULT_NUM_DAYS_AFTER_REDACTING_TRADE_DATA);
        }
        closeMyOfferWhenTakenPin.unbind();
        maxTradePriceDeviationPin.unbind();
        tradeAgeForRedactingDataPin.unbind();
    }
}
