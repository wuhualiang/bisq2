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

package bisq.user.reputation;

import bisq.bonded_roles.bonded_role.AuthorizedBondedRolesService;
import bisq.common.data.ByteArray;
import bisq.common.util.MathUtils;
import bisq.network.NetworkService;
import bisq.network.p2p.services.data.storage.auth.authorized.AuthorizedDistributedData;
import bisq.user.banned.BannedUserService;
import bisq.user.identity.UserIdentityService;
import bisq.user.profile.UserProfile;
import bisq.user.profile.UserProfileService;
import bisq.user.reputation.data.AuthorizedBondedReputationData;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
@Slf4j
public class BondedReputationService extends SourceReputationService<AuthorizedBondedReputationData> {
    public static final long WEIGHT = 10;
    public static final long LOCK_TIME = 50_000;

    public BondedReputationService(NetworkService networkService,
                                   UserIdentityService userIdentityService,
                                   UserProfileService userProfileService,
                                   BannedUserService bannedUserService,
                                   AuthorizedBondedRolesService authorizedBondedRolesService) {
        super(networkService, userIdentityService, userProfileService, bannedUserService, authorizedBondedRolesService);
    }

    @Override
    protected Optional<AuthorizedBondedReputationData> findRelevantData(AuthorizedDistributedData authorizedDistributedData) {
        return authorizedDistributedData instanceof AuthorizedBondedReputationData ?
                Optional.of((AuthorizedBondedReputationData) authorizedDistributedData) :
                Optional.empty();
    }

    @Override
    protected ByteArray getDataKey(AuthorizedBondedReputationData data) {
        return new ByteArray(data.getHash());
    }

    @Override
    protected ByteArray getUserProfileKey(UserProfile userProfile) {
        return userProfile.getBondedReputationKey();
    }

    @Override
    public long calculateScore(AuthorizedBondedReputationData data) {
        checkArgument(data.getLockTime() >= LOCK_TIME);
        return doCalculateScore(data.getAmount());
    }

    public static long doCalculateScore(long amount) {
        checkArgument(amount >= 0);
        return MathUtils.roundDoubleToLong(amount / 100d * WEIGHT);
    }
}