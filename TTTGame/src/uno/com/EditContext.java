package uno.com;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

public class EditContext extends LinearLayout{
	EditText editText;
	public EditContext(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater li = (LayoutInflater) this.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.edit_text, this, true);
	}

}
