/**
 * InfectStatistic
 * TODO
 *
 * @author 221701318
 * @version xxx
 * @since xxx
 */
import java.util.*;
import java.text.Collator;
import java.util.regex.*;
import  java.io.*;
import  java.lang.*;
class Province implements Comparable<Province>
{
    private String province;
    private int ip = 0,sp = 0,cure = 0,dead = 0;
    public Province(String province)
    {
        this.province = province;
    }
    public void setprovince(String pro)
    {
        this.province = pro;
    }


    final List<String> provinceorder=Arrays.asList("安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东",
            "山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江");
    public int compareTo(Province a)
    {
        String pro1 = this.province;
        String province2 = a.province;
        Collator instance = Collator.getInstance(Locale.CHINA);
        if(pro1.equals("全国")) return -1;
        else if(province2.equals("全国")) return 1;
        return instance.compare(pro1,province2);
    }


    public String getprovince()
    {
        return this.province;
    }
    public void setip(int ip)
    {
        this.ip = ip;
    }
    public int getip()
    {
        return this.ip;
    }
    public void setsp(int sp)
    {
        this.sp = sp;
    }
    public int getsp()
    {
        return this.sp;
    }
    public void setcure(int cure)
    {
        this.cure = cure;
    }
    public int getcure()
    {
        return this.cure;
    }
    public void setdead(int dead)
    {
        this.dead = dead;
    }
    public int getdead()
    {
        return this.dead;
    }
}

class InfectStatistic {

    public static void Add(String provence,List<Province> list1) // 判断列表中有没有该省份，无则加入
    {
        boolean inlist = false;
        for (Province province : list1) {
            inlist = provence.equals(province.getprovince());
            if(inlist) break ;
        }
        if(!inlist) // 若列表里没有该省份，加入列表
        {
            Province pro1 = new Province(provence);
            list1.add(pro1);
        }
    }

    public static void InforPro(String Path,List<Province> list1,Province Apro) //日志信息处理
    {
        int people;
        String pattern = ".*//.*";
        String pattern0 = "(\\d+)";
        String pattern1 = ".*新增.*";
        String pattern2 = ".*流入.*";
        String pattern3 = ".*死亡.*";
        String pattern4 = ".*治愈.*";
        String pattern5 = ".*确诊感染.*";
        String pattern6 = ".*排除.*";
        String pattern7 = ".*感染患者.*";
        String pattern8 = ".*疑似患者.*";
        Pattern number = Pattern.compile(pattern0); // 创建 Pattern 对象
        try
        {
            File file = new File(Path);
            if(!file .exists())
            {
                System.out.println("错误的路径");
                System.exit(0);
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = null;
            while ((str = br.readLine()) != null)
            {
                Matcher num = number.matcher(str);
                String arrays[] = str.split(" ");
                if(Pattern.matches(pattern,str)) continue;
                Add(arrays[0],list1);
                for (Province pro1 : list1) {
                    if (arrays[0].equals(pro1.getprovince())&&num.find())
                    {
                        people = Integer.valueOf(num.group(0));
                        if(Pattern.matches(pattern1,str))
                        {
                            if(Pattern.matches(pattern7,str))
                            {
                                pro1.setip(pro1.getip() + people);
                                Apro.setip(Apro.getip() + people);
                            }
                            else if(Pattern.matches(pattern8,str))
                            {
                                pro1.setsp(pro1.getsp() + people);
                                Apro.setsp(Apro.getsp() + people);
                            }

                        }
                        else if(Pattern.matches(pattern2,str))
                        {
                            Add(arrays[3],list1);
                            for (Province province2 : list1) {
                                if (arrays[3].equals(province2.getprovince()))
                                {
                                    if(Pattern.matches(pattern7,str))
                                    {
                                        pro1.setip(pro1.getip() - people);
                                        province2.setip(province2.getip() + people);
                                    }
                                    else if(Pattern.matches(pattern8,str))
                                    {
                                        pro1.setsp(pro1.getsp() - people);
                                        province2.setsp(province2.getsp() + people);
                                    }
                                }
                            }
                        }
                        else if(Pattern.matches(pattern3,str))
                        {
                            pro1.setdead(pro1.getdead() + people);
                            pro1.setip(pro1.getip() - people);
                            Apro.setdead(Apro.getdead() + people);
                            Apro.setip(Apro.getip() - people);
                        }
                        else if(Pattern.matches(pattern4,str))
                        {
                            pro1.setcure(pro1.getcure() + people);
                            pro1.setip(pro1.getip() - people);
                            Apro.setcure(Apro.getcure() + people);
                            Apro.setip(Apro.getip() - people);
                        }
                        else if(Pattern.matches(pattern5,str))
                        {
                            pro1.setip(pro1.getip() + people);
                            pro1.setsp(pro1.getsp() - people);
                            Apro.setip(Apro.getip() + people);
                            Apro.setsp(Apro.getsp() - people);
                        }
                        else if(Pattern.matches(pattern6,str))
                        {
                            pro1.setsp(pro1.getsp() - people);
                            Apro.setsp(Apro.getsp() - people);
                        }
                    }
                }
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public static void InforOut(String out,List<Province> list1,List<String> outinforo)
    {
        out = out.trim();
        String Path = out.substring(0,out.lastIndexOf("\\"));
        try {
            File file = new File(Path);
            if (!file.exists())
            {
                System.out.println("错误的路径");
                System.exit(0);
            }
            BufferedWriter wr = new BufferedWriter(new FileWriter(out));
            for (Province province : list1)
            {
                if (outinforo.isEmpty())
                {
                    wr.write(province.getprovince() + "共有感染患者" + province.getip() + "人 疑似患者" +province.getsp()
                            + "人 治愈" + province.getcure() + "人 死亡" + province.getdead() + "人\n");
                }
                else
                {
                    wr.write(province.getprovince() + "共有");
                    for (String str2 : outinforo)
                    {
                        if(str2.equals("ip"))  wr.write("感染患者" + province.getip() + "人 ");
                        if(str2.equals("sp")) wr.write("疑似患者" + province.getsp() + "人 ");
                        if(str2.equals("cure")) wr.write("治愈" + province.getcure() + "人 ");
                        if(str2.equals("dead")) wr.write("死亡" + province.getdead() + "人 ");
                    }
                    wr.write("\n");
                }
            }
            wr.write("// 该文档并非真实数据，仅供测试使用");
            wr.flush();
            wr.close();
        }catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public static void GetName(String path,ArrayList<String> listFileName) //得到日志名字
    {
        String patterndate = "\\d+-\\d+-\\d+";
        Pattern date = Pattern.compile(patterndate); // 创建 Pattern 对象
        File file = new File(path);
        String [] names = file.list();
        if(names != null){
            String [] completNames = new String[names.length];
            for(int i=0;i < names.length;i++){
                Matcher Date = date.matcher(names[i]); // 创建 matcher 对象
                if(Date.find()) completNames[i] = Date.group(0);
            }
            listFileName.addAll(Arrays.asList(completNames));
        }
    }
    public static void AddType(String[] args,int j,List<String> outinforo)
    {
        for(int i = j + 1;i < args.length;i++)
        {
            if(!args[i].contains("-")) outinforo.add(args[i]);
            else break;
        }
    }



    public static void Addpro(String[] args,int j,List<Province> ProInfor,List<Province> list1)
    {
        for(int i = j + 1;i < args.length;i++)
        {
            if(!args[i].contains("-"))
            {
                Add(args[i],list1);
                Add(args[i],ProInfor);
            }
            else break;
        }
    }
    public static void main(String[] args)
    {
        String log = null,date = null,out = null;
        int judge1 = 0,judge2 = 0,judge3 = 0;
        List<Province> list1 = new ArrayList<>();
        ArrayList<String> listFileName = new ArrayList<>();
        ArrayList<String> outinforo = new ArrayList<>();
        ArrayList<Province> ProInfor = new ArrayList<>();
        Province Apro = new Province("全国"); // 创建全国对象
        list1.add(Apro);
        for(int j = 0;j < args.length;j++) {
            if(args[j].equals("list")) judge1++;
            else if(args[j].equals("-log"))
            {
                log = args[j + 1 ];
                judge1++;
            }
            else if(args[j].equals("-out"))
            {
                out = args[j + 1];
                judge1++;
            }
            else if(args[j].equals("-date")) date = args[j + 1];
            else if(args[j].equals("-type"))
            {
                judge3 = 1;
                AddType(args,j,outinforo);
            }
            else if(args[j].equals("-provence"))
            {
                judge2 = 1;
                Addpro(args,j,ProInfor,list1);
            }
        }
        if(judge1!=3||(judge2==0&&!ProInfor.isEmpty())||(judge2==1&&ProInfor.isEmpty())
                ||(judge3==0&&!outinforo.isEmpty())||(judge3==1&&outinforo.isEmpty()))
        {
            System.out.println("非法命令");
            System.exit(0);
        }
        GetName(log,listFileName);
        for(String name:listFileName)
        {
            if(date==null)
            {
                InforPro(log+name+".log.txt",list1,Apro);
                continue;
            }
            else if(listFileName.get(listFileName.size()-1).compareTo(date)<=0)
            {
                System.out.println("输入的日期有错，请重新输入");
                System.exit(0);
            }
            else if (listFileName.get(0).compareTo(date) > 0) break;
            else if(name.compareTo(date) > 0) break;
            InforPro(log+name+".log.txt",list1,Apro);
        }
        for(Province provence : list1)
        {
            for(Province pro : ProInfor)
            {
                if(provence.getprovince().equals(pro.getprovince()))
                {
                    pro.setip(provence.getip());
                    pro.setsp(provence.getsp());
                    pro.setcure(provence.getcure());
                    pro.setdead(provence.getdead());
                }
            }
        }
        Collections.sort(list1);
        Collections.sort(ProInfor);
        if(judge2==1) InforOut(out,ProInfor,outinforo);
        else InforOut(out,list1,outinforo);
    }
}
