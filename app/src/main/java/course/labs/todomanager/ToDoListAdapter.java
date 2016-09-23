package course.labs.todomanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
//import course.labs.todomanager.doItemUpdate;

public class ToDoListAdapter extends BaseAdapter {

	private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();
	private final Context mContext;
	private final ToDoManagerActivity mMgr;

	private static final String TAG = "Lab-UserInterface";

	public ToDoListAdapter(Context context, ToDoManagerActivity mgr) {

		mContext = context;
		mMgr = mgr;

	}

	// Add a ToDoItem to the adapter
	// Notify observers that the data set has changed

	public void add(ToDoItem item) {

		mItems.add(item);
		notifyDataSetChanged();

	}

    public void remove(ToDoItem item) {

        mItems.remove(item);
        notifyDataSetChanged();

    }

	// Clears the list adapter of all items.

	public void clear() {

		mItems.clear();
		notifyDataSetChanged();

	}

	// Returns the number of ToDoItems

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Retrieve the number of ToDoItems

	@Override
	public Object getItem(int pos) {

		return mItems.get(pos);

	}

	// Get the ID for the ToDoItem
	// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}

	// TODO - Create a View for the ToDoItem at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.
    // Consider using the ViewHolder pattern to make scrolling more efficient
    // See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View toDoItemView;
		// TODO - Get the current ToDoItem
		final ToDoItem toDoItem = (ToDoItem)getItem(position);


		// TODO - Inflate the View for this ToDoItem
		// from todo_item.xml.

		if(convertView== null)
		{
			LayoutInflater inflater = (LayoutInflater) mContext
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			toDoItemView = inflater.inflate(R.layout.todo_item, parent, false);
		}
		else
		{
			toDoItemView = convertView;
		}
		

		// TODO - Fill in specific ToDoItem data
		// Remember that the data that goes in this View
		// corresponds to the user interface elements defined
		// in the layout file

		// TODO - Display Title in TextView
		final TextView title = (TextView)toDoItemView.findViewById(R.id.titleView);
		
		title.setText(toDoItem.getTitle());

		// TODO - Set up Status CheckBox
        final CheckBox ovdView = (CheckBox)toDoItemView.findViewById(R.id.overDueCheckBox);
        final CheckBox statusView = (CheckBox)toDoItemView.findViewById(R.id.statusCheckBox);;
        statusView.setChecked(toDoItem.getStatus().equals(ToDoItem.Status.DONE));
        // TODO - Must also set up an OnCheckedChangeListener,
        // which is called when the user toggles the status checkbox

        statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Log.i(TAG,"Entered onCheckedChanged()");
                        if(isChecked)
                            toDoItem.setStatus(ToDoItem.Status.DONE);
                        else
                            toDoItem.setStatus(ToDoItem.Status.NOTDONE);

                        ovdView.setChecked(toDoItem.getStatus() == ToDoItem.Status.NOTDONE &&
                                toDoItem.mDate.before(new Date()));
                    }
				});

        ovdView.setChecked(toDoItem.getStatus() == ToDoItem.Status.NOTDONE &&
                toDoItem.mDate.before(new Date()));

		// TODO - Display Priority in a TextView
        final TextView priority = (TextView)toDoItemView.findViewById(R.id.priorityView);
        priority.setText(toDoItem.getPriority().toString());
		// TODO - Display Time and Date
        
        final TextView dateAndTime = (TextView)toDoItemView.findViewById(R.id.dateView);
        dateAndTime.setText(toDoItem.FORMAT.format(toDoItem.getDate()));

		toDoItemView.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
                String data = toDoItem.toLog();
                Log.i(TAG, "Item: " + data.replace(ToDoItem.ITEM_SEP, ", "));
				mMgr.doItemUpdate(toDoItem);
			}
		});

		// Return the View you just created
		return toDoItemView;
	}
}
