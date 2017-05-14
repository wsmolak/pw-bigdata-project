import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
  * Created by xwsmolak on 5/13/17.
  */
object ReadXMLData {
  def main(args:Array[String]) : Unit = {
    //configure and start SparkSession
    val spark = SparkSession
      .builder()
      .appName("ReadXMLData")
      .getOrCreate()

    //read Posts.xml
    val temp_posts_df = spark.read
      .format("com.databricks.spark.xml")
      .option("rowTag", "posts")
      .load("data/Posts.xml")
    val posts_df = temp_posts_df.selectExpr("explode(row) as row")
      .select("row._Id", "row._OwnerUserId", "row._Body", "row._AnswerCount", "row._CreationDate", "row._ParentId", "row._PostTypeId", "row._Title", "row._ViewCount", "row._Tags", "row._FavoriteCount")
      .toDF()
    posts_df.printSchema()

    //read Comments.xml
    val temp_comments_df = spark.read
      .format("com.databricks.spark.xml")
      .option("rowTag", "comments")
      .load("data/Comments.xml")
    val comments_df = temp_comments_df.selectExpr("explode(row) as row")
      .select("row._Id" ,"row._CreationDate", "row._PostId", "row._Text")
      .toDF()
    comments_df.printSchema()

    //read Tags.xml
    val temp_tags_df = spark.read
      .format("com.databricks.spark.xml")
      .option("rowTag", "tags")
      .load("data/Tags.xml")
    val tags_df = temp_tags_df.selectExpr("explode(row) as row")
      .select("row._Count", "row._Id", "row._TagName")
      .toDF()
    tags_df.printSchema()
    tags_df.select("_TagName", "_Count").orderBy(desc("_Count")).take(15).foreach(println)
  }
}
