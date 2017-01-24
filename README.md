- Unfortunately I had to use AsyncTasks
- It would be a good idea to use a RecyclerView instead of a ListView (and apply the ViewHolder pattern)
- It would be very good use a disk cache for the downloaded images (I added a simple memory cache). Or we could use Glide, Picasso...
- I would have used Retrofit + Gson
- We could cache the network requests
- It would be a good idea to the use MVP pattern (but for this very simple case would be an overkill)
- It would be a good idea to use Data Binding and MVVM for the ListView items

