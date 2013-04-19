namespace java cc.twittertools.thrift.gen

struct TResultSet {
  1: required list<Term> query_terms // Ordered list of indexed query terms
  2: required list<TResult> results, // Ordered list of tweet results
  3: required Index index, // Tweet and term statistics of the current index
}

sruct Term{
  1: required string stem, // Actual stem that represents this term in the index
  3: required i64 df, // Number of tweets that contain this term
  4: required i64 cf // Number of occurrences of this term in the index 
}

struct Index{
  1: required i64 tweet_count, // Number of tweets "N" in the current index (Time aware)
  2: required i64 term_count, // Number of terms "T" in the current index (Time aware)
  3: required i64 collection_length, // Sum of tweet length in the current index  (Time aware)
  4: required i64 term_max_frequency, // Maximum term frequency in the current index (Time aware)
  5: optional i64 tweet_avg_length, // Average tweet length in the current index  (Time aware)
  6: required i64 tweet_max_length, // Maximum  tweet length in the current index  (Time aware)
  7: required list<i64> tweet_distribution // The number of indexed tweets over the n last days/hours
}

struct TResult {
  1: required i64 id, // Unique identifier for this Tweet
  2: required double rsv, // Retrieval Status Value
  3: required string created_at, // UTC time when this Tweet was created
  4: required string text, // The actual UTF-8 text of the status update
  5: required User user, // The user who posted this Tweet
  6: required string lang, // Machine-detected language of the Tweet text
  7: required Coordinates coordinates, //Geographic location of this Tweet
  8: required Retweet retweet, // Original Tweet if this Tweet is retweeted
  9: required Status in_reply_to, // Original Tweet if this Tweet is a reply
  10: optional set<string> hashtags, // Hashtags which have been parsed out of the Tweet text
  11: optional set<string> user_mentions, // Twitter users mentioned in the text of the Tweet
  12: optional set<string> urls, // URLs included in the text of a Tweet
  13: optional i32 length, // Number of terms in this tweet
  13: optional list<i32> query_terms_tf, // Frequencies of query terms in this tweet
}

struct User{
  1: required string screen_name, // User's screen name
  2: required i32 followers_count, // The number of followers this account currently has,
  3: required i32 friends_count, // The number of users this account is following (AKA their "followings")
}
struct Coordinates{
  1: required double longitude,
  2: required double latitude
}

struct Retweet{
  1: required i64 id, // Unique identifier for this Tweet
  2: required string screen_name, // User's screen name
  3: required i32 retweet_count // Number of times this Tweet has been retweeted
}
struct Status{
  1: required i64 id, // Unique identifier for this Tweet
  2: required string screen_name, // User's screen name
}

struct TQuery {
  1: string text,
  2: i64 max_id,
  3: i32 num_results
}
 
exception TrecSearchException {
  1: string message
}
 
service TrecSearch {
  TResultSet search(1: TQuery query)
    throws (1: TrecSearchException error)
}
