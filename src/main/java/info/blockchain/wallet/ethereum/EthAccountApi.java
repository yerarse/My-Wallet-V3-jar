package info.blockchain.wallet.ethereum;

import info.blockchain.wallet.BlockchainFramework;
import info.blockchain.wallet.ethereum.data.EthAddressResponse;
import info.blockchain.wallet.ethereum.data.EthAddressResponseMap;

import info.blockchain.wallet.ethereum.data.EthPushTxRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Call;

@SuppressWarnings("WeakerAccess")
public class EthAccountApi {

    private EthEndpoints endpoints;

    /**
     * Returns an {@link EthAddressResponse} object for a list of given ETH addresses as an {@link
     * Observable}. An {@link EthAddressResponse} contains a list of transactions associated with
     * the accounts, as well as a final balance for each.
     *
     * @param addresses The ETH addresses to be queried
     * @return An {@link Observable} wrapping an {@link EthAddressResponse}
     */
    public Observable<EthAddressResponseMap> getEthAddress(List<String> addresses) {
        return getApiInstance().getEthAccount(StringUtils.join(addresses, ","));
    }

    /**
     * Returns true if a given ETH address is associated with an Ethereum contract, which is
     * currently unsupported. This should be used to validate any proposed destination address for
     * funds.
     *
     * @param address The ETH address to be queried
     * @return An {@link Observable} returning true or false based on the address's contract status
     */
    public Observable<Boolean> getIfContract(String address) {
        return getApiInstance().getIfContract(address)
                .map(new Function<HashMap<String, Boolean>, Boolean>() {
                    @Override
                    public Boolean apply(HashMap<String, Boolean> map) throws Exception {
                        return map.get("contract");
                    }
                });
    }

    /**
     * Executes signed eth transaction and returns transaction hash.
     *
     * @param rawTx The ETH address to be queried
     * @return An {@link Observable} returning true or false based on the address's contract status
     */
    public Observable<String> pushTx(String rawTx) {
        return getApiInstance().pushTx(new EthPushTxRequest(rawTx))
            .map(new Function<HashMap<String, String>, String>() {
                @Override
                public String apply(HashMap<String, String> map) throws Exception {
                    return map.get("txHash");
                }
            });
    }

    /**
     * Lazily evaluates an instance of {@link EthEndpoints}.
     */
    private EthEndpoints getApiInstance() {
        if (endpoints == null) {
            endpoints = BlockchainFramework.getRetrofitApiInstance().
                    create(EthEndpoints.class);
        }
        return endpoints;
    }

}
