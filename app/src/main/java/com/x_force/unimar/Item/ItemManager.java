package com.x_force.unimar.Item;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.x_force.unimar.ProductListingActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemManager {
    static FirebaseFirestore db = ProductListingActivity.getDb();
    final static Query AllProductquery = db.collection("productListing");
    static Query productQuery = db.collection("productListing");
    static Query tutoringQuery = db.collection("tutorListing");
    public static ItemAdapter adapter;
    static ProductListCallback callback = new ProductListCallback() {
        public void onProductListLoaded(List<Item> items) {
            adapter.items = items;
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onProductListError(Exception e) {

        }
    };

    public static void addToList(Item item) {
        if (item instanceof Product) {
            db.collection("productListing").add(item).addOnSuccessListener(documentReference -> {
                item.setDocId(documentReference.getId());
                Log.d("Firestore", "Document added with Id: " + item.getDocId()); // sysout
            }).addOnFailureListener(e -> {
                Log.w("Firestore", "Error adding document!", e); //sysout
            });
        } else if (item instanceof Tutoring) {
            db.collection("tutorListing").add(item).addOnSuccessListener(documentReference -> {
                item.setDocId(documentReference.getId());
                Log.d("Firestore", "Document added with Id: " + item.getDocId());
            }).addOnFailureListener(e -> {
                Log.w("Firestore", "Error adding document!", e);// sysout
            });
        }
    }

    public static void deleteFromList(Item item) {
        if (item instanceof Product) {
            db.collection("productListing").document(item.getDocId()).delete().addOnSuccessListener(documentReference -> {
                Log.d("Firestore", "Document deleted with Id: " + item.getDocId());
            }).addOnFailureListener(e -> {
                Log.w("Firestore", "Error deleting document", e);
            });
        } else if (item instanceof Tutoring) {
            db.collection("tutorListing").document(item.getDocId()).delete().addOnSuccessListener(documentReference -> {
                        Log.d("Firestore", "Document deleted with Id: " + item.getDocId());
                    }
            ).addOnFailureListener(e -> {
                Log.w("Firestore", "Error deleting document", e);
            });
        }
    }

    public static List<Item> sortProductList(char c) { // eğer c 'A' karakteri ise artan fiyata göre sıralıyor. D--> azalan. İkisi de değilse sortlanmamış databaseyi yazdırıyor
        Query intialQuery = productQuery;
        ArrayList<Item> items = new ArrayList<>();
        c = Character.toUpperCase(c);
        if (c == 'A') {
            productQuery = productQuery.orderBy("cost", Query.Direction.ASCENDING);
        } else if (c == 'D') {
            productQuery = productQuery.orderBy("cost", Query.Direction.DESCENDING);
        }

        productQuery.get().addOnCompleteListener(done -> {
            if (done.isSuccessful()) {
                for (QueryDocumentSnapshot document : done.getResult()) {
                    Product product = document.toObject(Product.class);
                    items.add(product);
                    Log.d("Firestore", document.getId() + " => " + document.getData());
                }
                Log.d("Sort", items.size() + " ");
                callback.onProductListLoaded(items);
            } else {
                Log.w("Firestore", "Error getting documents: ", done.getException());
            }
        });

        productQuery = intialQuery;

        return items;
    }

    public static List<Item> sortTutorList(char c) {
        List<Item> items = Collections.emptyList();
        c = Character.toUpperCase(c);
        Query query;
        if (c == 'A') {
            query = db.collection("tutorListing").orderBy("cost", Query.Direction.ASCENDING);
        } else if (c == 'D') {
            query = db.collection("tutorListing").orderBy("cost", Query.Direction.DESCENDING);
        } else {
            query = db.collection("tutorListing");
        }
        query.get().addOnCompleteListener(done -> {
            if (done.isSuccessful()) {
                for (QueryDocumentSnapshot document : done.getResult()) {
                    Product product = document.toObject(Product.class);
                    items.add(product);
                    Log.d("Firestore", document.getId() + " => " + document.getData());
                }
            } else {
                Log.w("Firestore", "Error getting documents: ", done.getException());
            }
        });

        return items;
    }

    public static List<Item> filterList( char listType, double min, double max){

        listType = Character.toUpperCase(listType);
        List<Item> filteredList = new ArrayList<Item>();

        if( listType == 'P' ){
            productQuery = AllProductquery.whereGreaterThanOrEqualTo("cost",min).whereLessThanOrEqualTo("cost", max);;

            productQuery.get().addOnCompleteListener(done -> {
                if( done.isSuccessful() ) {
                    for (QueryDocumentSnapshot document : done.getResult()) {
                        Product product = document.toObject(Product.class);
                        filteredList.add(product);
                    }
                    callback.onProductListLoaded(filteredList);
                }
            }).addOnFailureListener(done ->{
                Log.w("Firestore", "Error getting documents: ", done.getCause());
            });
        }

        else if( listType == 'T' ){
            tutoringQuery = AllProductquery.whereGreaterThanOrEqualTo("cost",min).whereLessThanOrEqualTo("cost", max);

            tutoringQuery.get().addOnCompleteListener(done -> {
                if( done.isSuccessful() ) {
                    for (QueryDocumentSnapshot document : done.getResult()) {

                        Tutoring tutoring = document.toObject(Tutoring.class);
                        filteredList.add(tutoring);
                    }
                }
            });

        }

        return filteredList;

    }

    public static List<Item> filterList(char listType, ArrayList<String> searchCategory ){

        listType = Character.toUpperCase(listType);
        List<Item> searchedList = new ArrayList<Item>();
        String searchString = "";

        for( int i = 0 ; i < searchCategory.size() ; i++ ){
            searchString = searchCategory.get(i).toLowerCase();

            if( listType == 'P' ){
                productQuery = productQuery.whereArrayContains("category", searchString);

                productQuery.get().addOnCompleteListener(done -> {
                    if( done.isSuccessful() ) {
                        for (QueryDocumentSnapshot document : done.getResult()) {

                            Product product = document.toObject(Product.class);
                            searchedList.add(product);
                        }
                    }
                });
            }

            else if( listType == 'T' ){
                tutoringQuery = tutoringQuery.whereArrayContains("category", searchString);

                tutoringQuery.get().addOnCompleteListener(done -> {
                    if( done.isSuccessful() ) {
                        for (QueryDocumentSnapshot document : done.getResult()) {

                            Tutoring tutoring = document.toObject(Tutoring.class);
                            searchedList.add(tutoring);
                        }
                    }
                });

            }
        }

        return searchedList;
    }

    public static List<Item> searchInTheList(char listType, String itemName ){
        Query initialQuery = productQuery;
        listType = Character.toUpperCase(listType);
        //itemName = itemName.toLowerCase();  //bütün itemların ismi Item classında set edilirken otomatik lowercase oluyor, case karışıklığı engellemek için
        List<Item> searchedList = new ArrayList<Item>();

        if( listType == 'P' ) {
            productQuery = productQuery.whereGreaterThanOrEqualTo("name", itemName)
                    .whereLessThanOrEqualTo("name",itemName + "\uf8ff");

            productQuery.get().addOnCompleteListener(done -> {
                if( done.isSuccessful() ) {
                    for (QueryDocumentSnapshot document : done.getResult()) {
                        Product product = document.toObject(Product.class);
                        searchedList.add(product);
                    }
                    Log.d("ArrayLength", searchedList.size() + "");
                    callback.onProductListLoaded(searchedList);
                }
            });
            productQuery = initialQuery;
        }

        else if( listType == 'T' ) {
            tutoringQuery = tutoringQuery.whereEqualTo("name", itemName);

            tutoringQuery.get().addOnCompleteListener(done -> {
                if( done.isSuccessful() ) {
                    for (QueryDocumentSnapshot document : done.getResult()) {

                        Tutoring tutoring = document.toObject(Tutoring.class);
                        searchedList.add(tutoring);
                    }
                }
            });
        }

        return searchedList;

    }

    public static Query getProductQuery() {
        return productQuery;
    }

    public static void refreshProductQuery() {
        productQuery = db.collection("productListing");
    }

    public static Query getTutorQuery() {
        return tutoringQuery;
    }

    public static void refreshTutorQuery() {
        tutoringQuery = db.collection("tutorListing");
    }
}