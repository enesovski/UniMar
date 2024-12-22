package com.x_force.unimar.Item;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.x_force.unimar.ProductListingActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemManager {
    static FirebaseAuth auth = ProductListingActivity.getAuth();
    static FirebaseFirestore db = ProductListingActivity.getDb();
    final static Query AllProductquery = db.collection("productListing");
    final static Query AllTutoringquery = db.collection("tutorListing");
    static Query productQuery = db.collection("productListing");
    static Query tutoringQuery = db.collection("tutorListing");
    public static ItemAdapterParent adapter;
    static ProductListCallback callback = new ProductListCallback() {
        public void onProductListLoaded(List<Item> items) {
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onProductListError(Exception e) {

        }
    };

    static TutorListCallback callback2 = new TutorListCallback() {
        public void onTutorListLoaded(List<Item> items) {
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
        @Override
        public void onTutorListError(Exception e) {

        }
    };


    public static void addToList(Item item) {
         item.setUserId(Objects.requireNonNull(auth.getCurrentUser()).getUid());

        if (item instanceof Product) {
            db.collection("productListing").add(item).addOnSuccessListener(documentReference -> {
                item.setDocId(documentReference.getId());
                db.collection("productListing").document(item.getDocId()).update("docId",documentReference.getId());
                Log.d("MERABABABABABA", "Document added with Id: " + item.getDocId()); // sysout
            }).addOnFailureListener(e -> {
                Log.w("Firestore", "Error adding document!", e); //sysout
            });
        } else if (item instanceof Tutoring) {
            db.collection("tutorListing").add(item).addOnSuccessListener(documentReference -> {
                item.setDocId(documentReference.getId());
                db.collection("tutorListing").document(item.getDocId()).update("docId",documentReference.getId());
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

    public static List<Item> sortDeleteProductList(char c) { // eğer c 'A' karakteri ise artan fiyata göre sıralıyor. D--> azalan. İkisi de değilse sortlanmamış databaseyi yazdırıyor
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
                    if(product.getUserId() != null && product.getUserId().equals(auth.getUid())){
                        items.add(product);
                    }
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

    public static List<Item> sortDeleteTutorList(char c) { // eğer c 'A' karakteri ise artan fiyata göre sıralıyor. D--> azalan. İkisi de değilse sortlanmamış databaseyi yazdırıyor
        Query intialQuery = tutoringQuery;
        ArrayList<Item> items = new ArrayList<>();
        c = Character.toUpperCase(c);
        if (c == 'A') {
            tutoringQuery = tutoringQuery.orderBy("cost", Query.Direction.ASCENDING);
        } else if (c == 'D') {
            tutoringQuery = tutoringQuery.orderBy("cost", Query.Direction.DESCENDING);
        }

        tutoringQuery.get().addOnCompleteListener(done -> {
            if (done.isSuccessful()) {
                for (QueryDocumentSnapshot document : done.getResult()) {
                    Product product = document.toObject(Product.class);
                    if(product.getUserId() != null && product.getUserId().equals(auth.getUid())){
                        items.add(product);
                    }
                    Log.d("Firestore", document.getId() + " => " + document.getData());
                }
                Log.d("Sort", items.size() + " ");
                callback2.onTutorListLoaded(items);
            } else {
                Log.w("Firestore", "Error getting documents: ", done.getException());
            }
        });

        tutoringQuery = intialQuery;

        return items;
    }

    public static List<Item> sortTutorList(char c) {
        Query intialQuery = tutoringQuery;
        ArrayList<Item> items = new ArrayList<>();
        c = Character.toUpperCase(c);
        if (c == 'A') {
            tutoringQuery = tutoringQuery.orderBy("cost", Query.Direction.ASCENDING);
        } else if (c == 'D') {
            tutoringQuery = tutoringQuery.orderBy("cost", Query.Direction.DESCENDING);
        }

        tutoringQuery.get().addOnCompleteListener(done -> {
            if (done.isSuccessful()) {
                for (QueryDocumentSnapshot document : done.getResult()) {
                    Tutoring tutoring = document.toObject(Tutoring.class);
                    items.add(tutoring);
                    Log.d("Firestore", document.getId() + " => " + document.getData());
                }
                Log.d("Sort", items.size() + " ");
                callback2.onTutorListLoaded(items);
            } else {
                Log.w("Firestore", "Error getting documents: ", done.getException());
            }
        });

        tutoringQuery = intialQuery;

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
            tutoringQuery = AllTutoringquery.whereGreaterThanOrEqualTo("cost",min).whereLessThanOrEqualTo("cost", max);

            tutoringQuery.get().addOnCompleteListener(done -> {
                if( done.isSuccessful() ) {
                    for (QueryDocumentSnapshot document : done.getResult()) {
                        Tutoring tutoring = document.toObject(Tutoring.class);
                        filteredList.add(tutoring);
                    }
                    callback2.onTutorListLoaded(filteredList);
                }
            }).addOnFailureListener(done ->{
                Log.w("Firestore", "Error getting documents: ", done.getCause());
            });
        }
        return filteredList;
    }

    public static List<Item> filterListByRating( char listType, double min, double max){

        listType = Character.toUpperCase(listType);
        List<Item> filteredList = new ArrayList<Item>();

        if( listType == 'P' ){
            productQuery = AllProductquery.whereGreaterThanOrEqualTo("rating",min).whereLessThanOrEqualTo("rating", max);;

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
            tutoringQuery = AllTutoringquery.whereGreaterThanOrEqualTo("rating",min).whereLessThanOrEqualTo("rating", max);

            tutoringQuery.get().addOnCompleteListener(done -> {
                if( done.isSuccessful() ) {
                    for (QueryDocumentSnapshot document : done.getResult()) {

                        Tutoring tutoring = document.toObject(Tutoring.class);
                        filteredList.add(tutoring);
                    }
                    //callback yaz
                }
            });
        }
        return filteredList;
    }

    public interface CategoryCallBack {
        void onCallback(List<Item> resultList);
    }

    public static void filterList(char listType, ArrayList<String> searchCategory, CategoryCallBack callback) {
        listType = Character.toUpperCase(listType);
        List<Item> searchedList = new ArrayList<>();

        int[] completedQueries = {0};

        for (String searchString : searchCategory) {
            Log.d("aranan kategori", "kategori: " + searchString);

            //Query hashmap gibi ama daha efektif bizim için
            Query query;
            if (listType == 'P') {
                query = db.collection("productListing").whereArrayContains("category", searchString);
            } else if (listType == 'T') {
                query = db.collection("tutorListing").whereArrayContains("category", searchString);
            } else {
                continue;
            }

            char finalListType = listType;
            query.get().addOnCompleteListener(done -> {
                if (done.isSuccessful()) {
                    for (QueryDocumentSnapshot document : done.getResult()) {
                        if (finalListType == 'P') {
                            Product product = document.toObject(Product.class);
                            searchedList.add(product);
                            Log.d("aranan kategori", "eklendi:");
                        } else {
                            Tutoring tutoring = document.toObject(Tutoring.class);
                            searchedList.add(tutoring);
                        }
                    }
                }
                completedQueries[0]++;

                if (completedQueries[0] == searchCategory.size()) {
                    Log.d("callbackenes", ""+ searchedList.size());
                    callback.onCallback(searchedList);
                }
            });
        }
    }

    public static List<Item> searchInTheList(char listType, String itemName ){
        Query initialProductQuery = productQuery;
        Query initialTutorQuery = tutoringQuery;
        listType = Character.toUpperCase(listType);
        List<Item> searchedList = new ArrayList<Item>();

        if( listType == 'P' ) {
            if(itemName.equals(""))
            {
                Query query = productQuery;
                List<Item> finalList = new ArrayList<Item>();

                query.get().addOnCompleteListener(done -> {
                    if( done.isSuccessful() ) {
                        for (QueryDocumentSnapshot document : done.getResult()) {
                            Product product = document.toObject(Product.class);
                            finalList.add(product);
                        }
                        Log.d("ArrayLength", finalList.size() + "");
                        callback.onProductListLoaded(finalList);
                    }
                });

                return finalList;
            }

            itemName = itemName.toUpperCase();

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
            productQuery = initialProductQuery;
        }

        else if( listType == 'T' ) {
            if(itemName.equals(""))
            {
                Query query = tutoringQuery;
                List<Item> finalList = new ArrayList<Item>();

                query.get().addOnCompleteListener(done -> {
                    if( done.isSuccessful() ) {
                        for (QueryDocumentSnapshot document : done.getResult()) {
                            Product product = document.toObject(Product.class);
                            finalList.add(product);
                        }
                        Log.d("ArrayLength", finalList.size() + "");
                        callback2.onTutorListLoaded(finalList);
                    }
                });

                return finalList;
            }

            itemName = itemName.toUpperCase();

            tutoringQuery = tutoringQuery.whereGreaterThanOrEqualTo("name", itemName)
                    .whereLessThanOrEqualTo("name",itemName + "\uf8ff");

            tutoringQuery.get().addOnCompleteListener(done -> {
                if( done.isSuccessful() ) {
                    for (QueryDocumentSnapshot document : done.getResult()) {
                        Tutoring tutoring = document.toObject(Tutoring.class);
                        searchedList.add(tutoring);
                    }
                    callback2.onTutorListLoaded(searchedList);
                }
            });

            tutoringQuery = initialTutorQuery;
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

