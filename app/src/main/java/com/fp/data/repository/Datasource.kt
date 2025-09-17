package com.fp.data.repository

import com.fp.R
import com.fp.ui.store.Product
import com.fp.ui.store.ProductWrapper

/**
 * [Datasource] generates a list of [Affirmation]
 */


//https://dummyjson.com/products?limit=30&skip=0&select=title,description,category,price,rating,stock,thumbnail
class Datasource() {


    fun loadProductsWrapper(): List<ProductWrapper> {
        val productsWrapperList = Datasource().loadProducts().mapIndexed { index, product ->
            ProductWrapper(product = product, id = index, expanded = false)
        }
        return productsWrapperList
    }

    fun loadProducts(): List<Product> {
        return listOf<Product>(
            // Producto 1 ya existente
            Product(
                1,
                "Essence Mascara Lash Princess",
                "The Essence Mascara Lash Princess is a popular mascara known for its volumizing and lengthening effects. Achieve dramatic lashes with this long-lasting and cruelty-free formula.",
                "beauty",
                9.99,
                2.56,
                99,
                "https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/thumbnail.webp"
            ),
            // Productos añadidos a partir del JSON comentado
            Product(
                2,
                "Eyeshadow Palette with Mirror",
                "The Eyeshadow Palette with Mirror offers a versatile range of eyeshadow shades for creating stunning eye looks. With a built-in mirror, it's convenient for on-the-go makeup application.",
                "beauty",
                19.99,
                2.86,
                34,
                "https://cdn.dummyjson.com/product-images/beauty/eyeshadow-palette-with-mirror/thumbnail.webp"
            ),
            Product(
                3,
                "Powder Canister",
                "The Powder Canister is a finely milled setting powder designed to set makeup and control shine. With a lightweight and translucent formula, it provides a smooth and matte finish.",
                "beauty",
                14.99,
                4.64,
                89,
                "https://cdn.dummyjson.com/product-images/beauty/powder-canister/thumbnail.webp"
            ),
            Product(
                4,
                "Red Lipstick",
                "The Red Lipstick is a classic and bold choice for adding a pop of color to your lips. With a creamy and pigmented formula, it provides a vibrant and long-lasting finish.",
                "beauty",
                12.99,
                4.36,
                91,
                "https://cdn.dummyjson.com/product-images/beauty/red-lipstick/thumbnail.webp"
            ),
            Product(
                5,
                "Red Nail Polish",
                "The Red Nail Polish offers a rich and glossy red hue for vibrant and polished nails. With a quick-drying formula, it provides a salon-quality finish at home.",
                "beauty",
                8.99,
                4.32,
                79,
                "https://cdn.dummyjson.com/product-images/beauty/red-nail-polish/thumbnail.webp"
            ),
            Product(
                6,
                "Calvin Klein CK One",
                "CK One by Calvin Klein is a classic unisex fragrance, known for its fresh and clean scent. It's a versatile fragrance suitable for everyday wear.",
                "fragrances",
                49.99,
                4.37,
                29,
                "https://cdn.dummyjson.com/product-images/fragrances/calvin-klein-ck-one/thumbnail.webp"
            ),
            Product(
                7,
                "Chanel Coco Noir Eau De",
                "Coco Noir by Chanel is an elegant and mysterious fragrance, featuring notes of grapefruit, rose, and sandalwood. Perfect for evening occasions.",
                "fragrances",
                129.99,
                4.26,
                58,
                "https://cdn.dummyjson.com/product-images/fragrances/chanel-coco-noir-eau-de/thumbnail.webp"
            ),
            Product(
                8,
                "Dior J'adore",
                "J'adore by Dior is a luxurious and floral fragrance, known for its blend of ylang-ylang, rose, and jasmine. It embodies femininity and sophistication.",
                "fragrances",
                89.99,
                3.8,
                98,
                "https://cdn.dummyjson.com/product-images/fragrances/dior-j'adore/thumbnail.webp"
            ),
            Product(
                9,
                "Dolce Shine Eau de",
                "Dolce Shine by Dolce & Gabbana is a vibrant and fruity fragrance, featuring notes of mango, jasmine, and blonde woods. It's a joyful and youthful scent.",
                "fragrances",
                69.99,
                3.96,
                4,
                "https://cdn.dummyjson.com/product-images/fragrances/dolce-shine-eau-de/thumbnail.webp"
            ),
            Product(
                10,
                "Gucci Bloom Eau de",
                "Gucci Bloom by Gucci is a floral and captivating fragrance, with notes of tuberose, jasmine, and Rangoon creeper. It's a modern and romantic scent.",
                "fragrances",
                79.99,
                2.74,
                91,
                "https://cdn.dummyjson.com/product-images/fragrances/gucci-bloom-eau-de/thumbnail.webp"
            )
        )
    }

}