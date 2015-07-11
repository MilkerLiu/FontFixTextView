package com.example.testdemod;

import android.content.Context;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

/**
 * 字体自适应的TextView
 * @author Milker
 * @email liuwenhua@hoge.cn
 */
public class FontFixTextView extends EditText {
	private float preferredTextSize;// 计算后的文本字体大小
	private TextPaint paintCopy;
	private float mSpacingMult = 1.0f;
	private float mSpacingAdd = 0.0f;

	public FontFixTextView(Context context) {
		this(context, null);
	}

	public FontFixTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FontFixTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.preferredTextSize = this.getTextSize();
		paintCopy = new TextPaint(getPaint());
	}

	private void refitText(String text, int textWidth) {
		if (textWidth <= 0 || text == null || text.length() == 0)
			return;
		paintCopy.setTextSize(preferredTextSize);
		StaticLayout layout = new StaticLayout(text, paintCopy, getWidth(), Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, true);

		while (layout.getHeight()<getHeight()) {
			preferredTextSize += 3;
			paintCopy.setTextSize(preferredTextSize);
			layout = new StaticLayout(text, paintCopy, getWidth(), Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, true);
		}

		while (layout.getHeight()>getHeight() || layout.getLineWidth(0) > getWidth()) {
			preferredTextSize -= 3;
			paintCopy.setTextSize(preferredTextSize);
			layout = new StaticLayout(text, paintCopy, getWidth(), Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, true);
		}
		
		this.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.preferredTextSize);
	}

	@Override
	protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
		this.refitText(text.toString(), getWidth());
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
		if (width != oldwidth)
			this.refitText(this.getText().toString(), width);
	}
}