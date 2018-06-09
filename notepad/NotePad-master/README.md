# NotePad应用的功能扩展
## 1.1基本要求
###获取Note创建时间，添加时间戳
获取Note创建时的系统时间（最终修改时间），并对时间的显示格式进行统一化要求，以列表的形式将Note的标题，图标和所获取的创建时间显示出来， 代码：
<br>
```Java

  <TextView
        android:id="@+id/text1_time"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textAppearance="?android:attr/textAppearanceSmall"
        android:paddingLeft="5dip"
        android:textColor="@color/colorBlack"/>
```  
如下图：<br>
![](https://github.com/qinxingseven/experiment/blob/master/notepad/NotePad-master/picture/timestamp.png)<br>

## 1.2 NotePad搜索功能实现  
从数据库中获取搜索结果，实现Note的标题搜索功能，
代码：  
<br>
```Java
<item
    android:id="@+id/menu_search"
    android:title="@string/menu_search"
    android:icon="@android:drawable/ic_search_category_default"
    android:showAsAction="always">
</item>
```  
如下图：  <br>
![](https://github.com/qinxingseven/experiment/blob/master/notepad/NotePad-master/picture/search.png)<br>




## 2.Notepad笔记排序
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
如下图：<br>
![](https://github.com/qinxingseven/experiment/blob/master/notepad/NotePad-master/picture/create.png)<br>

### 笔记排序----按修改时间顺序排序
如下图：<br>
![](https://github.com/qinxingseven/experiment/blob/master/notepad/NotePad-master/picture/alter.png)<br>

## 3 Note输出功能  
添加了输出文件的功能，可以将Note文件提取出来，效果展示：  
![](https://github.com/qinxingseven/experiment/blob/master/notepad/NotePad-master/picture/output.png)<br>





