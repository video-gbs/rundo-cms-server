package com.runjian.auth.server.domain.vo.tree;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AppIdTree
 * @Description ID
 * @date 2023-02-08 周三 18:10
 */
public class AppIdTree {

    List<AppInfo> appInfo;

    class AppInfo {
        private String appId;
        private String appName;

        private List<MenuInfo> menuInfo;

        class MenuInfo {
            private String menuId;
            private String menuName;

            List<ApiInfo> apiInfo;

            class ApiInfo {
                private String apiId;
                private String apiName;

                public String getApiId() {
                    return apiId;
                }

                public void setApiId(String apiId) {
                    this.apiId = apiId;
                }

                public String getApiName() {
                    return apiName;
                }

                public void setApiName(String apiName) {
                    this.apiName = apiName;
                }
            }

            public String getMenuId() {
                return menuId;
            }

            public void setMenuId(String menuId) {
                this.menuId = menuId;
            }

            public String getMenuName() {
                return menuName;
            }

            public void setMenuName(String menuName) {
                this.menuName = menuName;
            }

            public List<ApiInfo> getApiInfo() {
                return apiInfo;
            }

            public void setApiInfo(List<ApiInfo> apiInfo) {
                this.apiInfo = apiInfo;
            }
        }
    }

    public List<AppInfo> getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(List<AppInfo> appInfo) {
        this.appInfo = appInfo;
    }
}

