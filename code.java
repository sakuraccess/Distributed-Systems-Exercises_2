from flask
import Flask, request, jsonify

app = Flask(__name__)

# Sample data
for books(can be replaced with a database)
books = [{
        "id": 1,
        "title": "1984",
        "author": "George Orwell",
        "genre": "Dystopian",
        "price": 9.99
    },
    {
        "id": 2,
        "title": "To Kill a Mockingbird",
        "author": "Harper Lee",
        "genre": "Classic",
        "price": 7.99
    },
    {
        "id": 3,
        "title": "The Great Gatsby",
        "author": "F. Scott Fitzgerald",
        "genre": "Classic",
        "price": 10.99
    },
]

cart = [] # Stores books added to the cart
orders = [] # Stores completed orders

API_KEY = "secure-api-key"
# Simple authentication mechanism

def authenticate():
    api_key = request.headers.get("API-Key")
if api_key != API_KEY:
    return jsonify({
        "error": "Unauthorized access"
    }), 401

@app.route("/books", methods = ["GET"])
def get_books():
    return jsonify(books)

@app.route("/books/<int:book_id>", methods = ["GET"])
def get_book(book_id):
    book = next((b
        for b in books
        if b["id"] == book_id), None)
if book:
    return jsonify(book)
return jsonify({
    "error": "Book not found"
}), 404

@app.route("/cart", methods = ["POST"])
def add_to_cart():
    auth = authenticate()
if auth:
    return auth

book_id = request.json.get("book_id")
book = next((b
    for b in books
    if b["id"] == book_id), None)
if not book:
    return jsonify({
        "error": "Book not found"
    }), 404

cart.append(book)
return jsonify({
    "message": "Book added to cart",
    "cart": cart
})

@app.route("/orders", methods = ["POST"])
def place_order():
    auth = authenticate()
if auth:
    return auth

if not cart:
    return jsonify({
        "error": "Cart is empty"
    }), 400

order = {
    "order_id": len(orders) + 1,
    "items": cart.copy()
}
orders.append(order)
cart.clear()
return jsonify({
    "message": "Order placed successfully",
    "order": order
})

if __name__ == "__main__":
    app.run(debug = True)