package phase2.trade.command;

import phase2.trade.permission.PermissionSet;

public interface PermissionBased {

    boolean checkPermission();

    PermissionSet getPermissionRequired();

}
