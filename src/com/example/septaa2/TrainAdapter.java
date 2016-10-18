package com.example.septaa2;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class TrainAdapter extends ArrayAdapter<Train> {

	private List<Train> objects;

	public TrainAdapter(Context context, int resource,
			List<Train> objects) {
		super(context, resource, objects);
		this.objects = objects;
		// TODO Auto-generated constructor stub
	}
	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */public View getView(int position, View convertView, ViewGroup parent){

			// assign the view we are converting to a local variable
			View v = convertView;

			// first check to see if the view is null. if so, we have to inflate it.
			// to inflate it basically means to render, or show, the view.
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.item_list, null);
			}

			/*
			 * Recall that the variable position is sent in as an argument to this method.
			 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
			 * iterates through the list we sent it)
			 * 
			 * Therefore, i refers to the current Item object.
			 */
			Train i = objects.get(position);

			if (i != null) {

				// This is how you obtain a reference to the TextViews.
				// These TextViews and ImageView are created in the XML files we defined.

				TextView trainNo = (TextView) v.findViewById(R.id.trainno);
				TextView dpTime = (TextView) v.findViewById(R.id.dpTime);
				TextView avTime = (TextView) v.findViewById(R.id.avTime);
				// check to see if each individual textview  is null.
				// if not, assign some text!
				if (trainNo != null){
					trainNo.setText(i.getTrainno());
				}
				if (dpTime != null){
					dpTime.setText(i.getDpTime());
				}
				if (avTime != null){
					avTime.setText(  i.getAvTime());
				}
				
				}
			
				
			
			

			// the view must be returned to our activity
			return v;

		}
	// show The Image
	


}

