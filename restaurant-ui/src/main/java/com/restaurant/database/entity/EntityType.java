package com.restaurant.database.entity;

import org.apache.commons.compress.utils.Sets;

import java.util.Set;

import static com.restaurant.database.entity.ActionType.*;

public enum EntityType {
    FOOD_ITEM(Sets.newHashSet(CREATE_FOOD_ITEM, UPDATE_FOOD_ITEM, DELETE_FOOD_ITEM)),
    ORDER(Sets.newHashSet(CREATE_ORDER, UPDATE_ORDER, DELETE_FOOD_ITEM)),
    USER(Sets.newHashSet(CREATE_USER, UPDATE_USER_DETAILS, UPDATE_USER_PASSWORD, DELETE_USER));

    private final Set<ActionType> actionTypes;

    EntityType(Set<ActionType> actionTypes){
        this.actionTypes = actionTypes;
    }

    public Set<ActionType> getActionTypes() {
        return actionTypes;
    }
}
