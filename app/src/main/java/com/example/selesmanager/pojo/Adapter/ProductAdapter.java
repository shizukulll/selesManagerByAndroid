package com.example.selesmanager.pojo.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selesmanager.R;
import com.example.selesmanager.pojo.Product;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public ProductAdapter(Context context) {
        this.context = context;
    }

    public ProductAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public void setProducts(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public void setProduct(Product product) {
        if (productList == null) {
            productList = new ArrayList<>();
        } else {
            productList.clear();
        }
        productList.add(product);
        notifyDataSetChanged();
    }

    public int getProductIdAtPosition(int position) {
        if (productList != null && position >= 0 && position < productList.size()) {
            return productList.get(position).getId();
        }
        return -1;
    }

    public void deleteProduct(int position) {
        if (productList != null && position >= 0 && position < productList.size()) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView productNameTextView;
        private TextView productPriceTextView;
        private TextView productCountTextView;
        private TextView productBrandTextView;
        private TextView productIdTextView;
        private OnItemClickListener onItemClickListener;

        public ProductViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productCountTextView = itemView.findViewById(R.id.productCountTextView);
            productBrandTextView = itemView.findViewById(R.id.productBrandTextView);
            productIdTextView = itemView.findViewById(R.id.productIdTextView);
            this.onItemClickListener = onItemClickListener;
            itemView.setBackgroundResource(R.drawable.view_border);

            itemView.setOnClickListener(this);
        }

        public void bind(Product product) {
            productNameTextView.setText(product.getName());
            productPriceTextView.setText("价格：" + String.valueOf(product.getPrice()));
            productCountTextView.setText("数量：" + String.valueOf(product.getCount()));
            productBrandTextView.setText("品牌：" + product.getBrand());
            productIdTextView.setText("商品ID：" + String.valueOf(product.getId()));
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position);
                }
            }
        }
    }
}
