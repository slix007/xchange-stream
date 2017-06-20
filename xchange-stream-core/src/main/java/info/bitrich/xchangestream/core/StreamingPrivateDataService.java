package info.bitrich.xchangestream.core;

import info.bitrich.xchangestream.core.dto.PrivateData;

import io.reactivex.Observable;

/**
 * Created by Sergey Shurmin on 6/10/17.
 */
public interface StreamingPrivateDataService {

    /**
     * Emits private data on subscription.
     */
    Observable<PrivateData> getAllPrivateDataObservable();
}
