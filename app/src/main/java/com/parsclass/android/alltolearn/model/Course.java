package com.parsclass.android.alltolearn.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import android.text.TextUtils;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.DateConverter;
import com.parsclass.android.alltolearn.Utils.ListStringConverter;
import com.parsclass.android.alltolearn.repository.CourseRepository;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Entity(tableName = "course_table")
@TypeConverters({DateConverter.class,ListStringConverter.class})
public class Course implements Serializable {
    @PrimaryKey
    private int id;

    private String server_id;
    private String  title;
    private String shortDescription;
    private String longDescription;
    private String level;
    private String imagePath;
    private String imagePreview;
    private String numberPersonRate;
    private float ratingAvg;
    private String duration;// unit is minute
    private String price;
    private String previousPrice;
    private String updateDate;
    private boolean isBestSeller;
    private String certificate;
    private String dateEndDiscount;
    private String numberEnroll;
    private String percentOff;
    private boolean started;

    private ArrayList<String> requirements;
    private String typeListCourse;
    private Date lastRefresh;
    private ArrayList<String> infoCourse;
    private ArrayList<String> whatLearn;
    private String instructorName;
    private int countLectures;
    private ArrayList<String> percentageEachRating;
    private String linkWebSite;
    private String urlIntroLecture;


//    @TypeConverters({CommentConverter.class})
//    private ArrayList<Comment> comments;

//    @TypeConverters({InstructorConverter.class})
//    private ArrayList<Instructor> instructors;

//    @TypeConverters({QandAConverter.class})
//    private ArrayList<Question> questions;

//    @TypeConverters({SectionConverter.class})
//    private ArrayList<Section> sections;


    public Course(int id, String title, String shortDescription, String longDescription, String level, String imagePath, String imagePreview, String numberPersonRate) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.level = level;
        this.imagePath = imagePath;
        this.imagePreview = imagePreview;
        this.numberPersonRate = numberPersonRate;
    }

    public Course(int id, String title, String shortDescription, String longDescription, String level, String imagePath, String imagePreview, String numberPersonRate, float ratingAvg, String duration, String price, String previousPrice, String updateDate, boolean isBestSeller, String dateEndDiscount, String percentOff, ArrayList<String> requirements, String typeListCourse, ArrayList<String> infoCourse, ArrayList<String> whatLearn, String instructorName, int countLectures, ArrayList<String> percentageEachRating) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.level = level;
        this.imagePath = imagePath;
        this.imagePreview = imagePreview;
        this.numberPersonRate = numberPersonRate;
        this.ratingAvg = ratingAvg;
        this.duration = duration;
        this.price = price;
        this.previousPrice = previousPrice;
        this.updateDate = updateDate;
        this.isBestSeller = isBestSeller;
        this.dateEndDiscount = dateEndDiscount;
        this.percentOff = percentOff;
        this.requirements = requirements;
        this.typeListCourse = typeListCourse;
        this.infoCourse = infoCourse;
        this.whatLearn = whatLearn;
        this.instructorName = instructorName;
        this.countLectures = countLectures;
        this.percentageEachRating = percentageEachRating;
    }

    public Course(String title, String imagePath, String numberPersonRate, float ratingAvg) {
        this.title = title;
        this.imagePath = imagePath;
        this.numberPersonRate = numberPersonRate;
        this.ratingAvg = ratingAvg;
        this.typeListCourse= CourseRepository.SLIDER;
    }

    public Course(String title, String imagePath, String numberPersonRate, float ratingAvg,String price) {
        this.title = title;
        this.imagePath = imagePath;
        this.numberPersonRate = numberPersonRate;
        this.ratingAvg = ratingAvg;
        this.typeListCourse= CourseRepository.TOP_COURSE_ONE;
        this.price=price;
    }

    public Course(int id, String title, String imagePath, String numberPersonRate, float ratingAvg, String price, String instructorName) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.numberPersonRate = numberPersonRate;
        this.ratingAvg = ratingAvg;
        this.price = price;
        this.instructorName = instructorName;
        this.typeListCourse=CourseRepository.STUDENT_ALSO_VIEWED;
    }

    public Course() {
        this.typeListCourse= CourseRepository.SLIDER;
    }

    public String getUrlIntroLecture() {
        return urlIntroLecture;
    }

    public void setUrlIntroLecture(String urlIntroLecture) {
        this.urlIntroLecture = urlIntroLecture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getNumberPersonRate() {
        return numberPersonRate;
    }

    public void setNumberPersonRate(String numberPersonRate) {
        this.numberPersonRate = numberPersonRate;
    }

    public float getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(float ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isBestSeller() {
        return isBestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        isBestSeller = bestSeller;
    }
    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public ArrayList<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<String> requirements) {
        this.requirements = requirements;
    }

    public String getTypeListCourse() {
        return typeListCourse;
    }

    public void setTypeListCourse(String typeListCourse) {
        this.typeListCourse = typeListCourse;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getImagePreview() {
        return imagePreview;
    }

    public void setImagePreview(String imagePreview) {
        this.imagePreview = imagePreview;
    }

    public String getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(String previousPrice) {
        this.previousPrice = previousPrice;
    }

    public String getDateEndDiscount() {
        return dateEndDiscount;
    }

    public void setDateEndDiscount(String dateEndDiscount) {
        this.dateEndDiscount = dateEndDiscount;
    }

    public ArrayList<String> getInfoCourse() {
        return infoCourse;
    }

    public ArrayList<String> getWhatLearn() {
        return whatLearn;
    }

    public void setWhatLearn(ArrayList<String> whatLearn) {
        this.whatLearn = whatLearn;
    }

    public void setInfoCourse(ArrayList<String> infoCourse) {
        this.infoCourse = infoCourse;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public int getCountLectures() {
        return countLectures;
    }

    public void setCountLectures(int countLectures) {
        this.countLectures = countLectures;
    }

    public ArrayList<String> getPercentageEachRating() {
        return percentageEachRating;
    }

    public void setPercentageEachRating(ArrayList<String> percentageEachRating) {
        this.percentageEachRating = percentageEachRating;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getNumberEnroll() {
        return numberEnroll;
    }

    public void setNumberEnroll(String numberEnroll) {
        this.numberEnroll = numberEnroll;
    }

    public String getPercentOff() {
        return percentOff;
    }

    public void setPercentOff(String percentOff) {
        this.percentOff = percentOff;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public String getLinkWebSite() {
        return linkWebSite;
    }

    public void setLinkWebSite(String linkWebSite) {
        this.linkWebSite = linkWebSite;
    }

    public String remainDaysToDiscount(Context context){
        if(TextUtils.isEmpty(dateEndDiscount)){
            return "";
        }
        String format = "MM/dd/yyyy hh:mm:ss";
        Date dateObj1=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            dateObj1 = sdf.parse(dateEndDiscount);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        Date dateObj2=cal.getTime();
        long diffInMillies =dateObj1.getTime()- dateObj2.getTime();

        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        Map<TimeUnit,Long> resultMap = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;

        for ( TimeUnit unit : units ) {

            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;

            resultMap.put(unit,diff);
        }
        long diffDays=resultMap.get(TimeUnit.DAYS);
        long diffhours=resultMap.get(TimeUnit.HOURS);
        long diffmin=resultMap.get(TimeUnit.MINUTES);

        String result="";
        if(diffDays!=0){
            result+=String.valueOf(diffDays)+" "+context.getString(R.string.day)+" "+context.getString(R.string.txt_and)+" ";
        }
        if(diffhours!=0){
            result+=String.valueOf(diffhours)+" "+context.getString(R.string.label_hour)+" "+context.getString(R.string.txt_and)+" ";
        }
        if(diffmin!=0){
            result+=String.valueOf(diffmin)+" "+context.getString(R.string.label_min)+" ";
        }

        //String result=String.valueOf(diffDays)+" روز و "+String.valueOf(diffhours)+" ساعت و "+String.valueOf(diffmin)+" دقیقه تا پایان تخفیف باقی مانده!";
         result+=context.getString(R.string.label_remain_days);
        return result;
    }

    public String getHourDuration(){
        String result="";
        int hour=Integer.parseInt(duration)/60;
        if(hour!=0)
            result+=String.valueOf(hour);
        return result;
    }

    public String getMinDuration(){
        String result="";
        int min=Integer.parseInt(duration)%60;
        if(min!=0)
            result+=String.valueOf(min);
        return result;
    }

    public String getHourMinDuration(Context context){
        String result="";
        int hour=Integer.parseInt(duration)/60;
        int min=Integer.parseInt(duration)%60;
        if(hour!=0 && min!=0){
            result+=String.valueOf(hour)+" "+context.getString(R.string.label_hour)+" "+context.getString(R.string.txt_and)+" "+String.valueOf(min)+" "+context.getString(R.string.label_min);
        }
        else if(hour!=0 && min==0){
            result+=String.valueOf(hour)+" "+context.getString(R.string.label_hour);
        }
        else if(min!=0 && hour==0){
            result+=String.valueOf(min)+" "+context.getString(R.string.label_min);
        }

        return result;
    }

    public String curriculum(Context context){
        String result="";
        result+="("+countLectures+") "+ context.getString(R.string.txt_lecture)+" - "+context.getString(R.string.txt_generally)+" (";
        if(!TextUtils.isEmpty(getHourDuration())){
            result+=getHourDuration()+context.getString(R.string.label_hour)+" ";
        }
        if(!TextUtils.isEmpty(getMinDuration())){
            result+=context.getString(R.string.txt_and)+" "+getMinDuration()+context.getString(R.string.label_min);
        }
        result+=")";
        return result;
    }

    public boolean emptyPreviousPrice(){
        if(TextUtils.isEmpty(previousPrice)){
            return true;
        }
        else
            return false;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", numberPersonRate='" + numberPersonRate + '\'' +
                ", ratingAvg=" + ratingAvg +
                '}';
    }

    private String newsImg, newsTitle;

    public String getNewsImg() {
        return newsImg;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsImg(String newsImg) {
        this.newsImg = newsImg;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public Course(String newsTitle, String newsImg) {
        this.newsImg = newsImg;
        this.newsTitle = newsTitle;
    }

    public static DiffUtil.ItemCallback<Course> DIFF_CALLBACK = new DiffUtil.ItemCallback<Course>() {
        @Override
        public boolean areItemsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            return oldItem.newsTitle.equals(newItem.newsTitle);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Course article = (Course) obj;
        return article.newsTitle.equals(this.newsTitle);
    }
}
