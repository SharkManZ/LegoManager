package ru.shark.home.legomanager.dao.dto.request;

import ru.shark.home.common.services.dto.PageRequest;

public class UserPartRequestDto extends PageRequest {
    boolean onlyIntroduced;

    public boolean isOnlyIntroduced() {
        return onlyIntroduced;
    }

    public void setOnlyIntroduced(boolean onlyIntroduced) {
        this.onlyIntroduced = onlyIntroduced;
    }
}
