package cn.edu.fzu.daoyun_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.fzu.daoyun_app.R;
import cn.edu.fzu.daoyun_app.bean.TreeBean;
import cn.edu.fzu.daoyun_app.holder.BaseTelecomHolder;
import cn.edu.fzu.daoyun_app.holder.LeafViewHolder;
import cn.edu.fzu.daoyun_app.holder.TreeItemClickListener;
import cn.edu.fzu.daoyun_app.holder.TrunkHolder;

public class TreeAdapter extends RecyclerView.Adapter<BaseTelecomHolder> {

    private Context context;
    private List<TreeBean> dataBeanList;
    private LayoutInflater mInflater;
    private OnScrollListener mOnScrollListener;

    private boolean isExpand = false;
    private TreeBean tempExpandTreeBean = null;



    //学校数据测试样例
    //TODO  测试样例 待删
    private static List<String> shcoolList = new ArrayList<String>() {//这个大括号 就相当于我们  new 接口
        {//这个大括号 就是 构造代码块 会在构造函数前 调用
            this.add("福州大学");//this 可以省略  这里加上 只是为了让读者 更容易理解
            add("福建师范大学");
            add("福建理工大学");
        }
    };

    //获取学校测试样例
    public static List<String> getShcoolList() {
        return shcoolList;
    }

    private static ArrayList<List<String>> facultyList = new ArrayList<List<String>>() {
        {
            this.add(Arrays.asList("石油化工学院", "数学与计算机科学学院", "物理与信息工程学院"));
            add(Arrays.asList("法学院", "外国语学院", "音乐学院"));
            add(Arrays.asList("化工", "数计", "人文"));
        }
    };
    public static ArrayList<List<String>> getFacultyList() {
        return facultyList;
    }
    public TreeAdapter(Context context, List<TreeBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
        this.mInflater = LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<TreeBean> dataBeanList) {
        this.dataBeanList = dataBeanList;
        notifyDataSetChanged();
    }

    @Override
    public BaseTelecomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        //根据返回的类型生成viewHolder
        switch (viewType) {
            case TreeBean.ONELIST:
                view = mInflater.inflate(R.layout.item_trunk, parent, false);
                return new TrunkHolder(context, view);
            case TreeBean.TWOLIST:
                view = mInflater.inflate(R.layout.item_leaf, parent, false);
                return new LeafViewHolder(context, view);
            default:
                view = mInflater.inflate(R.layout.item_trunk, parent, false);
                return new TrunkHolder(context, view);
        }
    }


    @Override
    public void onBindViewHolder(BaseTelecomHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TreeBean.ONELIST:
                TrunkHolder parentViewHolder = (TrunkHolder) holder;
                parentViewHolder.bindView(dataBeanList.get(position), position, itemClickListener);
                break;
            case TreeBean.TWOLIST:
                LeafViewHolder childViewHolder = (LeafViewHolder) holder;
                childViewHolder.bindView(dataBeanList.get(position), position);
                break;
        }
    }


    /**
     * 滚动监听接口
     */
    public interface OnScrollListener {
        void scrollTo(int pos);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }


    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataBeanList.get(position).getType();
    }

    private TreeItemClickListener itemClickListener = new TreeItemClickListener() {

        /**
         * @param bean 展开菜单栏的事件
         */
        @Override
        public void onExpandChildren(TreeBean bean) {

            //判断是否有已经展开的栏
            if (isExpand) {
                onHideChildren(tempExpandTreeBean);
                isExpand = false;
            }


            //TODO 设置二级菜单项
            int position = dataBeanList.lastIndexOf(bean);//确定当前点击的item位置
            List<TreeBean> rdblist = getLeafData(position, bean.getTrunk1());
            dataBeanList.get(position).setTreeBeanList(rdblist);
            //获取要展示的子布局数据对象，注意区分onHideChildren方法中的getChildBean()。
            if (rdblist == null || rdblist.size() == 0) {
                return;
            }
            for (int i = rdblist.size() - 1; i > -1; i--) {
                add(rdblist.get(i), position + 1);//在当前的item下方插入
            }
            if (position == dataBeanList.size() - 2 && mOnScrollListener != null) { //如果点击的item为最后一个
                mOnScrollListener.scrollTo(position + rdblist.size());//向下滚动，使子布局能够完全展示
            }
            //更新展开栏
            isExpand = true;
            tempExpandTreeBean = bean;
        }

        /**
         * @param bean 隐藏子菜单栏事件
         */
        @Override
        public void onHideChildren(TreeBean bean) {
            //说明当前的bean就是同一个 isExpan去除
            if (bean == tempExpandTreeBean) {
                isExpand = false;
            }
            bean.setExpand(false);
            int position = dataBeanList.lastIndexOf(bean);//确定当前点击的item位置
            List<TreeBean> children = bean.getTreeBeanList();//获取子布局对象
            if (children == null) {
                return;
            }
            //后面的孩子需要移除不然出错
            for (int i = 1; i < children.size() + 1; i++) {
                remove(position + 1);//删除
            }
            if (mOnScrollListener != null) {
                mOnScrollListener.scrollTo(position);
            }
        }
    };

    /**
     * @param position 获取树叶子的内容 同时添加
     * @return
     */
    @NonNull
    private List<TreeBean> getLeafData(int position, String fatherStunk) {
        List<TreeBean> rdblist = new ArrayList<>();
        List<String> tempList = TreeAdapter.getFacultyList().get(position);
        for (int j = 0; j < tempList.size(); j++) {
            TreeBean rdb = new TreeBean();
            rdb.setLeaf1(tempList.get(j));
            rdb.setTrunk1(fatherStunk);
            // rdb.setLeaf2("叶子2" + j);
            rdb.setType(TreeBean.TWOLIST);
            rdblist.add(rdb);
        }
        return rdblist;
    }

    /**
     * 在父布局下方插入一条数据
     *
     * @param bean
     * @param position
     */
    public void add(TreeBean bean, int position) {
        notifyItemInserted(position);
        dataBeanList.add(position, bean);
    }

    /**
     * 移除子布局数据
     *
     * @param position
     */
    protected void remove(int position) {
        if (position < dataBeanList.size()) {
            dataBeanList.remove(position);
            notifyItemRemoved(position);
        } else {
            System.out.println("remove 索引出错" + position);
        }
    }
}
