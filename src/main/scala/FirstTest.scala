import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by xwsmolak on 5/13/17.
  */
object FirstTest {
  def main(args:Array[String]) : Unit = {
    val conf = new SparkConf().setAppName("FirstTest").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val tf = sc.textFile("data/readme.txt")
    val splits = tf.flatMap(line => line.split(" ")).map(word => (word, 1))
    val counts = splits.reduceByKey((x, y) => x + y)
    splits.saveAsTextFile("output/splits")
    counts.saveAsTextFile("output/counts")
  }
}
