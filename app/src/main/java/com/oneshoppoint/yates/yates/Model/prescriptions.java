package com.oneshoppoint.yates.yates.Model;


import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.oneshoppoint.yates.yates.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;

/**
 * Created by stephineosoro on 03/06/16.
 */
public class Prescriptions extends CardWithList {
    private ArrayList<ItemDetails> itemDetailsrrayList;
    private String title;

    public Prescriptions(Context context, ArrayList<ItemDetails> items,String title) {
        super(context);
        this.itemDetailsrrayList = items;
        this.title=title;
    }

    @Override
    protected CardHeader initCardHeader() {

        //Add Header
        CardHeader header = new CardHeader(getContext(), R.layout.prescription_header);

        //Add a popup menu. This method set OverFlow button to visible
     /*   header.setPopupMenu(R.menu.popup_item, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_add:
                        //Example: add an item
                        PrescriptionObject w1= new PrescriptionObject(GoogleNowWeatherCard.this);
                        w1.feature ="Madrid";
                        w1.value = 24;
                        w1.weatherIcon = R.drawable.ic_action_sun;
                        w1.setObjectId(w1.feature);
                        mLinearListAdapter.add(w1);
                        break;
                    case R.id.action_remove:
                        //Example: remove an item
                        mLinearListAdapter.remove(mLinearListAdapter.getItem(0));
                        break;
                }

            }
        });*/
        header.setTitle(title); //should use R.string.
        return header;
    }

    @Override
    protected void initCard() {

        //Set the whole card as swipeable
       /* setSwipeable(true);
        setOnSwipeListener(new OnSwipeListener() {
            @Override
            public void onSwipe(Card card) {
                Toast.makeText(getContext(), "Swipe on " + card.getCardHeader().getTitle(), Toast.LENGTH_SHORT).show();
            }
        });*/

    }


    @Override
    protected List<ListObject> initChildren() {

        //Init the list
        List<ListObject> mObjects = new ArrayList<ListObject>();


        for(int i=0;i<itemDetailsrrayList.size();i++)
        {
            int j=0;
            j=i+1;
            PrescriptionObject infoObject=new PrescriptionObject(this);
            infoObject.feature=j+". "+"Name";
            infoObject.value=itemDetailsrrayList.get(i).getName();
            mObjects.add(infoObject);

            PrescriptionObject infoObject2=new PrescriptionObject(this);
            infoObject2.feature="note";
            infoObject2.value=itemDetailsrrayList.get(i).getItemDescription();
            mObjects.add(infoObject2);

            PrescriptionObject infoObject3=new PrescriptionObject(this);
            infoObject3.feature="Dosage Form";
            infoObject3.value=itemDetailsrrayList.get(i).getID();
            mObjects.add(infoObject3);

            PrescriptionObject infoObject4=new PrescriptionObject(this);
            infoObject4.feature="Quantity";
            infoObject4.value=itemDetailsrrayList.get(i).getQuantity();
            mObjects.add(infoObject4);

            PrescriptionObject infoObject5=new PrescriptionObject(this);
            infoObject5.feature="Freq. per day";
            infoObject5.value=itemDetailsrrayList.get(i).getTotal();
            mObjects.add(infoObject5);
            PrescriptionObject infoObject6=new PrescriptionObject(this);
            infoObject6.feature="Duration";
            infoObject6.value=itemDetailsrrayList.get(i).getEmail();
            mObjects.add(infoObject6);
        }




        return mObjects;
    }

    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {

        //Setup the ui elements inside the item
        TextView feature = (TextView) convertView.findViewById(R.id.pfeature);
        TextView value = (TextView) convertView.findViewById(R.id.pvalue);

        //Retrieve the values from the object
        PrescriptionObject pObject = (PrescriptionObject) object;

        feature.setText(pObject.feature);
        value.setText(pObject.value);

        return convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.prescription_inner;
    }


    // -------------------------------------------------------------
    // Weather Object
    // -------------------------------------------------------------

    public class PrescriptionObject extends DefaultListObject {

        public String feature;
        public String value;
        public String dosageform;
        public String frequencyQuantity;
        public String frequencyPerDay;




        public PrescriptionObject(Card parentCard) {
            super(parentCard);
            init();
        }

        private void init() {
            //OnClick Listener
           /* setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
                    Toast.makeText(getContext(), "Click on " + getObjectId(), Toast.LENGTH_SHORT).show();
                }
            });

            //OnItemSwipeListener
            setOnItemSwipeListener(new OnItemSwipeListener() {
                @Override
                public void onItemSwipe(ListObject object, boolean dismissRight) {
                    Toast.makeText(getContext(), "Swipe on " + object.getObjectId(), Toast.LENGTH_SHORT).show();
                }
            });*/
        }

    }
}
