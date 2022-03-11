package cc.banzhi.runmonitor.layer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cc.banzhi.runmonitor.R;
import cc.banzhi.runmonitor.dto.MemoryBean;

/**
 * @program: ZRunMonitor
 * @description: 内存View
 * @author: zoufengli01
 * @create: 2022/3/10 9:52 下午
 **/
public class MemoryView extends FrameLayout {
    private TextView tvTip;
    private TextView tvInfo;
    private PolylineView vPolyline;

    public MemoryView(@NonNull Context context) {
        this(context, null);
    }

    public MemoryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MemoryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.rmonitor_view_memory, this, true);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView();
    }

    private void initView() {
        tvTip = findViewById(R.id.tv_tip);
        tvInfo = findViewById(R.id.tv_info);
        findViewById(R.id.iv_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClose();
                }
            }
        });
        vPolyline = findViewById(R.id.v_polyline);
    }

    public void onBind(MemoryBean data) {
        if (data != null) {
            if (data.sysLowMemory) {
                tvTip.setText("⚠️ 内存低！");
                tvTip.setVisibility(VISIBLE);
                Toast.makeText(getContext(), "⚠️ 当前内存低！", Toast.LENGTH_SHORT).show();
            } else if (data.sysAvailMem <= data.sysThreshold) {
                tvTip.setText("⚠️ 内存不足！");
                tvTip.setVisibility(VISIBLE);
                Toast.makeText(getContext(), "⚠️ 当前内存不足！", Toast.LENGTH_SHORT).show();
            } else {
                tvTip.setVisibility(GONE);
            }
            tvInfo.setText(data.formatMem);

            // 更新折线图
            vPolyline.addPolyline(data.appAllocatedMem * 1.0f / data.appTotalMem);
        }
    }

    // 回调
    public interface ClickListener {
        void onClose();
    }

    private ClickListener listener;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }
}
