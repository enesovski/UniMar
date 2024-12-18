package com.x_force.unimar.Item;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.x_force.unimar.ProductDeletingActivity;
import com.x_force.unimar.R;
import com.x_force.unimar.Views.ProductView;

import java.util.List;

//Listede gözükecek her bir item için nasıl görüneceğini
public class ItemDeleteAdapter extends ArrayAdapter<Item> implements ItemAdapterParent{

    List<Item> items;
    public ItemDeleteAdapter(Context context, List<Item> items){
        super(context,0,items);
        this.items = items;
    }

    @NonNull
    @Override
    // Listedeki her item için ui'a göre bir element oluşturuyoruz.
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = items.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_product_delete, parent, false);
        }

        Button deletebutton = convertView.findViewById(R.id.delete);
        Button viewButton = convertView.findViewById(R.id.button_product_view);
        Button addNewButton = convertView.findViewById(R.id.button_change_activity_add);
        TextView nameTextView = convertView.findViewById(R.id.item_name);
        TextView costTextView = convertView.findViewById(R.id.item_cost);
        TextView descTextView = convertView.findViewById(R.id.item_desc);
        View gradientBar = convertView.findViewById(R.id.gradientBar);
        CardView card = convertView.findViewById(R.id.card);
        ImageView imageView = convertView.findViewById(R.id.item_image);

        if(item.getClass().equals(Product.class) && ((Product) item).getImage() != null){
            Bitmap imageBitmap= decodeBase64toBitmap(((Product) item).getImage());
            imageView.setImageBitmap(imageBitmap);
        }

        nameTextView.setText(item.getName());
        costTextView.setText(String.valueOf(item.getCost()));
        descTextView.setText(item.getDesc());

        TextView sortButton = convertView.findViewById(R.id.sort_button);
        TextView filterButton = convertView.findViewById(R.id.filter_button);
        SearchView searchBar = convertView.findViewById(R.id.product_searchbar);


        addNewButton.setVisibility(View.GONE);
        viewButton.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.GONE);
        sortButton.setVisibility(View.GONE);
        filterButton.setVisibility(View.GONE);
        gradientBar.setVisibility(View.GONE);
        card.setVisibility(View.VISIBLE);

        if(deletebutton!=null)
        {
            deletebutton.setVisibility(View.VISIBLE);
            deletebutton.setOnClickListener(e -> {

                ItemManager.deleteFromList(item);
                Intent intent = new Intent(getContext(), ProductDeletingActivity.class);
                getContext().startActivity(intent);
            });
        }

        viewButton.setOnClickListener(v -> {
            ItemManager.deleteFromList(item);
        });

        return convertView;
    }

    @Override
    public void setItems(List<Item> items) {
        this.items=items;
    }

    public static Bitmap decodeBase64toBitmap(String image) {
        byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        return decodedBitmap;
    }
}