package com.zee.listener;

import java.util.List;


public interface OnPermissionListener {
    /**
     * 返回申请权限的结果
     *
     * @param deniedPermissions，没有通过的权限,如果没有数据，那就是全部通过
     * @param permissionExplain                        权限的解释
     */
    void onPerMission(List<String> deniedPermissions, List<String> permissionExplain);
}
