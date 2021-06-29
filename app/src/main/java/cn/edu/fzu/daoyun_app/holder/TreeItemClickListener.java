package cn.edu.fzu.daoyun_app.holder;


import cn.edu.fzu.daoyun_app.bean.TreeBean;

public interface TreeItemClickListener {
    /**
     * 展开子Item
     *
     * @param bean
     */
    void onExpandChildren(TreeBean bean);

    /**
     * 隐藏子Item
     *
     * @param bean
     */
    void onHideChildren(TreeBean bean);
}
