# 基于NotePad应用的功能扩展
## 基本要求
### NoteList中显示条目增加时间戳显示
在NotePad原应用中，笔记列表只显示了笔记的标题。要对它做时间扩展，可以把时间放在标题的下方。
首先，找到列表中item的布局：noteslist_item.xml。要在标题下方加时间显示，就要在标题的TextView下再加一个时间的TextView。但是由于原应用列表item只需要一个标题，所以不需要用上别的布局，要多加一个时间TextView，就要把标题TextView和时间TextView放入垂直的线性布局。<br>
```Java
添加显示时间的TextView
    <TextView
        android:id="@+id/text1_time"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textAppearance="?android:attr/textAppearanceSmall"
        android:paddingLeft="5dip"
        android:textColor="@color/colorBlack"/>
```
把加入的时间戳改为以时间格式存入，改动地方分别为NotePadProvider中的insert方法和NoteEditor中的updateNote方法。前者为创建笔记时产生的时间，后者为修改笔记时产生的时间。下面代码中的dateTime即为转化后的时间格式，将其用ContentValues的put方法存入数据库。<br>
```Java
Long now = Long.valueOf(System.currentTimeMillis());
Date date = new Date(now);
SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
String dateTime = format.format(date);
```
运行效果：<br>
![](https://github.com/panwenxia/NotePad/blob/master/images/1.png)<br>
### 笔记查询（按标题查询）
要添加笔记查询功能，就要在应用中增加一个搜索的入口。找到菜单的xml文件，list_options_menu.xml，添加一个搜索的item。新建一个名为NoteSearch的activity，把SearchView跟ListView相结合，动态地显示搜索结果。<br>
```Java
<item
    android:id="@+id/menu_search"
    android:title="@string/menu_search"
    android:icon="@android:drawable/ic_search_category_default"
    android:showAsAction="always">
</item>
```
运行效果：<br>
![](https://github.com/panwenxia/NotePad/blob/master/images/2.png)<br>
## 扩展功能
笔记排序相对来说简单，只要把Cursor的排序参数变换下就可以了。在菜单文件list_options_menu.xml中添加：<br>
```Java
<item
    android:id="@+id/menu_sort"
    android:title="@string/menu_sort"
    android:icon="@android:drawable/ic_menu_sort_by_size"
    android:showAsAction="always" >
    <menu>
        <item
            android:id="@+id/menu_sort1"
            android:title="@string/menu_sort1"/>
        <item
            android:id="@+id/menu_sort2"
            android:title="@string/menu_sort2"/>
        <item
            android:id="@+id/menu_sort3"
            android:title="@string/menu_sort3"/>
        </menu>
    </item>
 ```
### 笔记排序----按创建时间顺序排序
```Java
//创建时间排序
    case R.id.menu_sort1:
        cursor = managedQuery(
                getIntent().getData(),            
                PROJECTION,                      
                null,                          
                null,                          
                NotePad.Notes._ID 
                );
        adapter = new MyCursorAdapter(
                this,
                R.layout.noteslist_item,
                cursor,
                dataColumns,
                viewIDs
        );
        setListAdapter(adapter);
        return true;
 ```
运行效果：<br>
![](https://github.com/panwenxia/NotePad/blob/master/images/3.png)<br>
### 笔记排序----按修改时间顺序排序
```
//修改时间排序
    case R.id.menu_sort2:
        cursor = managedQuery(
                getIntent().getData(),          
                PROJECTION,                      
                null,                            
                null,                       
                NotePad.Notes.DEFAULT_SORT_ORDER 
        );
        adapter = new MyCursorAdapter(
                this,
                R.layout.noteslist_item,
                cursor,
                dataColumns,
                viewIDs
        );
        setListAdapter(adapter);
        return true;
```
运行效果：<br>
![](https://github.com/panwenxia/NotePad/blob/master/images/4.png)<br>
### 导出笔记
导出文件是写在onPause()中的，点击保存响应的函数里调用了finish()，而onPause()会在finish()之后调用，但是，onPause()在点击手机的返回键退出当前activity也会调用，也就是说，如果没有经过特殊处理，点击手机返回键也会进行导出文件，这与预想不符。所以特别设置了一个flag用于判断是否是点击导出按钮，是点击按钮，则flag设置为true，可以进行文件导出，否则flag为默认的false，判断语句为假，不能执行到导出文件的操作。<br>
```Java
public class OutputText extends Activity {
   //要使用的数据库中笔记的信息
    private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID, // 0
            NotePad.Notes.COLUMN_NAME_TITLE, // 1
            NotePad.Notes.COLUMN_NAME_NOTE, // 2
            NotePad.Notes.COLUMN_NAME_CREATE_DATE, // 3
            NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, // 4
    };
    //读取出的值放入这些变量
    private String TITLE;
    private String NOTE;
    private String CREATE_DATE;
    private String MODIFICATION_DATE;
    //读取该笔记信息
    private Cursor mCursor;
    //导出文件的名字
    private EditText mName;
    //NoteEditor传入的uri，用于从数据库查出该笔记
    private Uri mUri;
    //关于返回与保存按钮的一个特殊标记，返回的话不执行导出，点击按钮才导出
    private boolean flag = false;
    private static final int COLUMN_INDEX_TITLE = 1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output_text);
        mUri = getIntent().getData();
        mCursor = managedQuery(
                mUri,        // The URI for the note that is to be retrieved.
                PROJECTION,  // The columns to retrieve
                null,        // No selection criteria are used, so no where columns are needed.
                null,        // No where columns are used, so no where values are needed.
                null         // No sort order is needed.
        );
        mName = (EditText) findViewById(R.id.output_name);
    }
    @Override
    protected void onResume(){
        super.onResume();
        if (mCursor != null) {
            // The Cursor was just retrieved, so its index is set to one record *before* the first
            // record retrieved. This moves it to the first record.
            mCursor.moveToFirst();
            //编辑框默认的文件名为标题，可自行更改
            mName.setText(mCursor.getString(COLUMN_INDEX_TITLE));
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mCursor != null) {
        //从mCursor读取对应值
            TITLE = mCursor.getString(mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_TITLE));
            NOTE = mCursor.getString(mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_NOTE));
            CREATE_DATE = mCursor.getString(mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_CREATE_DATE));
            MODIFICATION_DATE = mCursor.getString(mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE));
            //flag在点击导出按钮时会设置为true，执行写文件
            if (flag == true) {
                write();
            }
            flag = false;
        }
    }
    public void OutputOk(View v){
        flag = true;
        finish();
    }
    private void write()
    {
        try
        {
            // 如果手机插入了SD卡，而且应用程序具有访问SD的权限
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的目录
                File sdCardDir = Environment.getExternalStorageDirectory();
                //创建文件目录
                File targetFile = new File(sdCardDir.getCanonicalPath() + "/" + mName.getText() + ".txt");
                //写文件
                PrintWriter ps = new PrintWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
                ps.println(TITLE);
                ps.println(NOTE);
                ps.println("创建时间：" + CREATE_DATE);
                ps.println("最后一次修改时间：" + MODIFICATION_DATE);
                ps.close();
                Toast.makeText(this, "保存成功,保存位置：" + sdCardDir.getCanonicalPath() + "/" + mName.getText() + ".txt", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
```
运行效果：<br>
![](https://github.com/panwenxia/NotePad/blob/master/images/5.png)<br>
### UI美化
先给NoteList换个主题，把黑色换成白色，UI美化主要是让NoteList和NoteSearch每条笔记都有背景色，并且能保存,做到保存颜色的数据，最直接的办法就是在数据库中添加一个颜色的字段.在系统中预定于好五种颜色，根据颜色对应不int值选择要显示的颜色.将颜色填充到ListView，可以用SimpleCursorAdapter中的getView，bindView，newView方法来实现，我选择了bindView。自定义一个CursorAdapter继承SimpleCursorAdapter，既能完成cursor读取的数据库内容填充到item，又能将颜色填充：<br>
```Java
public class MyCursorAdapter extends SimpleCursorAdapter {
    public MyCursorAdapter(Context context, int layout, Cursor c,
                           String[] from, int[] to) {
        super(context, layout, c, from, to);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor){
        super.bindView(view, context, cursor);
        //从数据库中读取的cursor中获取笔记列表对应的颜色数据，并设置笔记颜色
        int x = cursor.getInt(cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_BACK_COLOR));
        switch (x){
            case NotePad.Notes.DEFAULT_COLOR:
                view.setBackgroundColor(Color.rgb(255, 255, 255));
                break;
            case NotePad.Notes.YELLOW_COLOR:
                view.setBackgroundColor(Color.rgb(247, 216, 133));
                break;
            case NotePad.Notes.BLUE_COLOR:
                view.setBackgroundColor(Color.rgb(165, 202, 237));
                break;
            case NotePad.Notes.GREEN_COLOR:
                view.setBackgroundColor(Color.rgb(161, 214, 174));
                break;
            case NotePad.Notes.RED_COLOR:
                view.setBackgroundColor(Color.rgb(244, 149, 133));
                break;
            default:
                view.setBackgroundColor(Color.rgb(255, 255, 255));
                break;
        }
    }
}
```
运行效果：<br>
![](https://github.com/panwenxia/NotePad/blob/master/images/7.png)<br>
### 背景更换
  背景更换指的是编辑笔记时的背景色更换。将UI美化与背景更换结合，让编辑笔记时的背景色跟笔记列表的该笔记背景色同为一种颜色。在NoteEditor类中有onResume()方法，onResume()方法在正常启动时会被调用，一般是onStart()后会执行onResume()，在Acitivity从Pause状态转化到Active状态也会被调用，利用这个特点，将从数据库读取颜色并设置编辑界面背景色操作放入其中，好处除了从笔记列表点进来时可以被执行到，跳到改变颜色Activity（接下来会提到）回来时也会被执行到。<br>
 ```
 //读取颜色数据
int x = mCursor.getInt(mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_BACK_COLOR));
    switch (x){
        case NotePad.Notes.DEFAULT_COLOR:
            mText.setBackgroundColor(Color.rgb(255, 255, 255));
            break;
        case NotePad.Notes.YELLOW_COLOR:
            mText.setBackgroundColor(Color.rgb(247, 216, 133));
            break;
        case NotePad.Notes.BLUE_COLOR:
            mText.setBackgroundColor(Color.rgb(165, 202, 237));
            break;
        case NotePad.Notes.GREEN_COLOR:
            mText.setBackgroundColor(Color.rgb(161, 214, 174));
            break;
        case NotePad.Notes.RED_COLOR:
            mText.setBackgroundColor(Color.rgb(244, 149, 133));
            break;
        default:
            mText.setBackgroundColor(Color.rgb(255, 255, 255));
            break;
    }
 ```
 因为选择颜色就会响应对应的函数，而函数先将选择的颜色信息保存在color变量中，调用finish()后会执行onPause()，在onPause()中将颜色存入数据库，Activity从NoteColor回到NoteEditor，NoteEditor被唤醒，会调用NoteEditor的onResume()，onResume()中有读取数据库颜色信息将设置背景的操作，就达到了换背景色的作用，并且也达到了NoteList中笔记颜色更改与编辑背景一致的效果。 <br>
 运行效果：<br>
![](https://github.com/panwenxia/NotePad/blob/master/images/6.png)<br>
