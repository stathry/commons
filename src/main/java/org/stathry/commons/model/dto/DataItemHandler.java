package org.stathry.commons.model.dto;

/**
 * DataItemHandler
 * Created by dongdaiming on 2018-12-18 17:17
 */
public interface DataItemHandler {

    Object handle(DataItem item, Object value);
}
