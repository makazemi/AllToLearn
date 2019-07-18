package com.parsclass.android.alltolearn.Utils;

import android.content.Context;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.model.CategoryItem;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.SubCategory;

import java.util.ArrayList;
import java.util.List;

public class CourseGenerator {

    List<Course> list=new ArrayList<>();
    public  List<CategoryItem> categoryItems=new ArrayList<>();
    private Context context;

    public List<Course> getFakeData(){
        return list;
    }

    public CourseGenerator(String type){
        for (int i=1;i<20;i++){
            addCourse(i,type);
        }
       // setCategory();
    }

    public CourseGenerator(String type, Context context){
        for (int i=1;i<20;i++){
            addCourse(i,type);
        }
        this.context=context;
        setCategory();
    }

    public List<CategoryItem> getCategory(){
        return categoryItems;
    }



    private void addCourse(int id,String typelistCourse){
        ArrayList<String> requirements=new ArrayList<>();
        ArrayList<String> infoCourse=new ArrayList<>();
        ArrayList<String> whatLearn=new ArrayList<>();
        ArrayList<String> percentageEachRating=new ArrayList<>();
        requirements.add("اچ تی ام ال");
        requirements.add("جاوا اسکریپت");
        requirements.add("جاوا اسکریپت");
        requirements.add("جاوا اسکریپت");
        requirements.add("جاوا اسکریپت");
        requirements.add("جاوا اسکریپت");
        infoCourse.add("اطلاعات دوره اطلاعات دوره");
        infoCourse.add("اطلاعات دوره اطلاعات دوره");
        infoCourse.add("اطلاعات دوره اطلاعات دوره");
        infoCourse.add("اطلاعات دوره اطلاعات دوره");
        whatLearn.add("یادگیری کامل برنامه نویسی وب");
        whatLearn.add("یادگیری کامل برنامه نویسی وب");
        whatLearn.add("یادگیری کامل برنامه نویسی وب");
        whatLearn.add("یادگیری کامل برنامه نویسی وب");
        whatLearn.add("یادگیری کامل برنامه نویسی وب");
        percentageEachRating.add("20");
        percentageEachRating.add("30");
        percentageEachRating.add("40");
        percentageEachRating.add("50");
        percentageEachRating.add("60");
        float rate=4;

        String imagPath="http://isha.sadhguru.org/blog/wp-content/uploads/2016/05/natures-temples.jpg";
        String longDescription=
                "        لورم ایپسوم یا طرCنما (به انگلیسی: Lorem ipsum) به متنی آزمایشی و بیu200Cمعنی در صنعت چاپ، صفحu200Cآرایی و طراحی گرافیک گفته میشود. طراح گرافیک از این متن به عنوان عنصری از ترکیب بندی برای پر کردن صفحه و ارایه اولیه شکل ظاهری و کلی طرح سفارش گرفته شده استفاده می نماید، تا از نظر گرافیکی نشانگر چگونگی نوع و اندازه فونت و ظاهر متن باشد. معمولا طرافا به مشتری یا صاحب کار خود نشان دهند که صفحه طراحی یا صفحه بندی شده بعد از اینکه متن در آن قرار گیرد چگونه به نظر می00Cرسد و قل0Cها و انداز00CبندیCها چگونه در نظر گرفته شد00Cاست. از آنجایی که طراحان عموما نویسنده متن نیستند و وظیفه رعایت حق تکثیر متون را ندارند و در همان حال کار آنها به نوعی وابسته به متن میCباشد آنها با استفاده از محتویات ساختگی، صفحه گرافیکی خود را صفحهu200Cآرایی میCکنند تا مرحله طراحی و صفحهCبندی را به پایان برند.";
        Course cc=new Course(id,"دوره ی کامل کامل کامل کامل برنامه نویسی ASP.NET",
                "توصیف مختصر دوره توصیف مختصر دوره توصیف مختصر دوره توصیف مختصر دوره",
                longDescription,"مقدماتی","",
                "","32,324");
        cc.setRatingAvg(rate);
        cc.setDuration("178");
        cc.setPrice("35000");
        cc.setPreviousPrice("50000");
        cc.setUpdateDate("5/5/2019 15:30:43");
        cc.setBestSeller(false);
        cc.setDateEndDiscount("05/24/2019 15:30:43");
        cc.setPercentOff("90");
        cc.setRequirements(requirements);
        cc.setTypeListCourse(typelistCourse);
        cc.setInfoCourse(infoCourse);
        cc.setWhatLearn(whatLearn);
        cc.setInstructorName("علیرضا عزیزی");
        cc.setCountLectures(20);
        cc.setPercentageEachRating(percentageEachRating);
        cc.setNumberEnroll("369");
        cc.setStarted(false);
        cc.setLinkWebSite("www.parsclass.com");
        cc.setUrlIntroLecture("https://hw14.cdn.asset.aparat.com/aparat-video/44371cbff17d8dacee0c727394c063c814535313-144p__81353.mp4");
//        Course course=new Course(id,"دوره ی کامل کامل کامل کامل برنامه نویسی ASP.NET",
//                "توصیف مختصر دوره توصیف مختصر دوره توصیف مختصر دوره توصیف مختصر دوره",
//                longDescription,"مقدماتی","http://isha.sadhguru.org/blog/wp-content/uploads/2016/05/natures-temples.jpg",
//                "http://isha.sadhguru.org/blog/wp-content/uploads/2016/05/natures-temples.jpg","32,324","25,345",
//                rate,"50","35000","50000","97/11/28",
//                false,"03/24/2019 15:30:43","375","60",requirements,typelistCourse,infoCourse,whatLearn,"علیرضا عزیزی",20,percentageEachRating);
        list.add(cc);
    }

    public void  setCategory(){
        ArrayList<SubCategory> subCategories=new ArrayList<>();
        subCategories.add(new SubCategory("شیرینی پزی"));
        subCategories.add(new SubCategory("کیک پزی"));
        subCategories.add(new SubCategory("دسر و ژله"));
        subCategories.add(new SubCategory("غذاهای سنتی"));
        subCategories.add(new SubCategory("آشپزی ملل"));
        subCategories.add(new SubCategory("آشپزی ملل"));
        subCategories.add(new SubCategory("آشپزی ملل"));
        subCategories.add(new SubCategory("آشپزی ملل"));
        subCategories.add(new SubCategory("آشپزی ملل"));
        int img1=R.drawable.img_category_it;
        int img2=R.drawable.img_category_english;
        int img3=R.drawable.img_category_art;
        int img4=R.drawable.img_category_health;
        int img5=R.drawable.img_category_businnes;
        int img6=R.drawable.img_category_sport;
        int img7=R.drawable.img_category_cooking;
        int img8=R.drawable.img_category_music;


        categoryItems.add(new CategoryItem(1,context.getString(R.string.computer),img1,subCategories,null));
        categoryItems.add(new CategoryItem(2,context.getString(R.string.leaerning_english),img2,subCategories,null));
        categoryItems.add(new CategoryItem(3,context.getString(R.string.art),img3,subCategories,null));
        categoryItems.add(new CategoryItem(4,context.getString(R.string.lifeStyle),img4,subCategories,null));
        categoryItems.add(new CategoryItem(5,context.getString(R.string.bussinus),img5,subCategories,null));
        categoryItems.add(new CategoryItem(6,context.getString(R.string.sport),img6,subCategories,null));
        categoryItems.add(new CategoryItem(7,context.getString(R.string.cooking),img7,subCategories,null));
        categoryItems.add(new CategoryItem(8,context.getString(R.string.music),img8,subCategories,null));

    }
}
