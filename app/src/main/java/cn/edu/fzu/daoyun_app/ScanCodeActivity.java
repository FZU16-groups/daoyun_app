package cn.edu.fzu.daoyun_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ScanCodeActivity  extends AppCompatActivity {

    lateinit var adapter: BarcodeAdapter
    private val datas = ArrayList<String>()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scan_code);
    }
    override fun initView() {
        img_menu.setOnClickListener(this)
        adapter = BarcodeAdapter(this,datas)
        ls_barcode.adapter = adapter
        ls_barcode.onItemClickListener = this
        lin_barcode.setOnClickListener(this)
        lin_qr_code.setOnClickListener(this)
        lin_about.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_menu -> drawerlayout.openDrawer(Gravity.LEFT)
            R.id.lin_barcode -> startActivity(Intent(this,BarCodeActivity::class.java))
            R.id.lin_qr_code -> startActivity(Intent(this,QRCodeActivity::class.java))
            R.id.lin_about -> startActivity(Intent(this,AboutActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        reScan()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        CopyUtil.copy(this,datas[position])
        Toast.makeText(this,resources.getString(R.string.copyed),Toast.LENGTH_LONG).show()
    }

    override fun scanResultCallBack(content: String) {
        datas.add(content)
        adapter.notifyDataSetChanged()
        if (datas.size == 1){
            Toast.makeText(this,resources.getString(R.string.click_copy),Toast.LENGTH_LONG).show()
            re_l.visibility = View.GONE
            ls_barcode.visibility = View.VISIBLE
        }

        ls_barcode.setSelection(adapter.count -1)
        playBeef()
        reScan()
    }
}
