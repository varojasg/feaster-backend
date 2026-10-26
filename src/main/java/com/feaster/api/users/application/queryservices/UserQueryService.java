package com.feaster.api.users.application.queryservices;

import com.feaster.api.users.domain.model.aggregates.User;
import com.feaster.api.users.application.queries.UserView;
import java.util.List;

public interface UserQueryService {
    User requireUser(String userKey);
    UserView toView(User user);
    UserView profile(String userKey);
    List<UserView> list();
}
