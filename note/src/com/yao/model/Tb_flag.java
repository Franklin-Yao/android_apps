package com.yao.model;

public class Tb_flag// ��ǩ��Ϣʵ����
{
    private int _id;// �洢��ǩ���
    private String flag;// �洢��ǩ��Ϣ
    private String title;

	public Tb_flag() {// Ĭ�Ϲ��캯��
        super();
    }

    // �����вι��캯����������ʼ����ǩ��Ϣʵ�����еĸ����ֶ�
    public Tb_flag(int id, String title,String flag) {
        super();
        this._id = id;// Ϊ��ǩ�Ÿ�ֵ
        this.title=title;
        this.flag = flag;// Ϊ��ǩ��Ϣ��ֵ
    }

    public int getid() {// ���ñ�ǩ��ŵĿɶ�����
        return _id;
    }

    public void setid(int id) {// ���ñ�ǩ��ŵĿ�д����
        this._id = id;
    }

    public String getFlag() {// ���ñ�ǩ��Ϣ�Ŀɶ�����
        return flag;
    }

    public void setFlag(String flag) {// ���ñ�ǩ��Ϣ�Ŀ�д����
        this.flag = flag;
    }
    public String getTitle() 
    {
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}
}
