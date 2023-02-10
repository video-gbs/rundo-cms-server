### 本系统都授权逻辑是将自身的拥有的角色权限，进行组合后分配给登陆者创建的下一级角色
#### 例如：
    1.systemAdmin作为整个系统的超级管理员角色，其拥有系统中所有的权限；
    2.systemAdmin为了减轻管理系统的工作压力，将自己的权限进行组合分配到N个新的角色：userAdmin，configAdmin，videoAdmin ···
    3.configAdmin