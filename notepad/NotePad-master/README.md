# NotePadӦ�õĹ�����չ
## 1.1����Ҫ��
###��ȡNote����ʱ�䣬���ʱ���
��ȡNote����ʱ��ϵͳʱ�䣨�����޸�ʱ�䣩������ʱ�����ʾ��ʽ����ͳһ��Ҫ�����б����ʽ��Note�ı��⣬ͼ�������ȡ�Ĵ���ʱ����ʾ������ ���룺
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
����ͼ��<br>
![](https://github.com/qinxingseven/experiment/blob/master/notepad/NotePad-master/picture/timestamp.png)<br>

## 1.2 NotePad��������ʵ��  
�����ݿ��л�ȡ���������ʵ��Note�ı����������ܣ�
���룺  
<br>
```Java
<item
    android:id="@+id/menu_search"
    android:title="@string/menu_search"
    android:icon="@android:drawable/ic_search_category_default"
    android:showAsAction="always">
</item>
```  
����ͼ��  <br>
![](https://github.com/qinxingseven/experiment/blob/master/notepad/NotePad-master/picture/search.png)<br>




## 2.Notepad�ʼ�����
### �ʼ�����----������ʱ��˳������
```Java
//����ʱ������
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
����ͼ��<br>
![](https://github.com/qinxingseven/experiment/blob/master/notepad/NotePad-master/picture/create.png)<br>

### �ʼ�����----���޸�ʱ��˳������
����ͼ��<br>
![](https://github.com/qinxingseven/experiment/blob/master/notepad/NotePad-master/picture/alter.png)<br>

## 3 Note�������  
���������ļ��Ĺ��ܣ����Խ�Note�ļ���ȡ������Ч��չʾ��  
![](https://github.com/qinxingseven/experiment/blob/master/notepad/NotePad-master/picture/output.png)<br>





