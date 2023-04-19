package me.kimsoar.springbook.service;

import me.kimsoar.springbook.model.User;

public interface UserLevelUpgradePolicy {

    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);

}
