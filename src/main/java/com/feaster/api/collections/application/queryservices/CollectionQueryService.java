package com.feaster.api.collections.application.queryservices;

import com.feaster.api.collections.domain.model.aggregates.GourmetCollection;
import java.util.List;

public interface CollectionQueryService {
    List<GourmetCollection> publicCollections();
    List<GourmetCollection> byOwner(String userKey);
    List<GourmetCollection> mine(String userKey);
    GourmetCollection visible(String collectionKey, String currentUserKey);
    GourmetCollection requireCollection(String collectionKey);
}
