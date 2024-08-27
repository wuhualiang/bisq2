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

package bisq.offer.amount.spec;

import bisq.common.proto.UnresolvableProtobufMessageException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * No min. amount supported
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class FixedAmountSpec implements AmountSpec {
    protected final long amount;

    public FixedAmountSpec(long amount) {
        this.amount = amount;
    }

    @Override
    public void verify() {
        checkArgument(amount > 0);
    }

    public bisq.offer.protobuf.FixedAmountSpec.Builder getFixedAmountSpecBuilder(boolean serializeForHash) {
        return bisq.offer.protobuf.FixedAmountSpec.newBuilder().setAmount(amount);
    }

    static FixedAmountSpec fromProto(bisq.offer.protobuf.FixedAmountSpec proto) {
        return switch (proto.getMessageCase()) {
            case BASESIDEFIXEDAMOUNTSPEC -> BaseSideFixedAmountSpec.fromProto(proto);
            case QUOTESIDEFIXEDAMOUNTSPEC -> QuoteSideFixedAmountSpec.fromProto(proto);
            case MESSAGE_NOT_SET -> throw new UnresolvableProtobufMessageException(proto);
        };
    }
}