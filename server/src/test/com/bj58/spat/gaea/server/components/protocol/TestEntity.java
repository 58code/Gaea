package com.bj58.spat.gaea.server.components.protocol;

public class TestEntity {

	private Boolean fBoolean;
	private Byte fByte;
	private Character fCharacter;
	private Double fDouble;
	private Float fFloat;
	private Integer fInteger;
	private Long fLong;
	private Short fShort;
	private String fString;
	private boolean fboolean;
	private byte fbyte;
	private char fchar;
	private double fdouble;
	private float ffloat;
	private int fint;
	private long flong;
	private short fshort;
	private Object fObject;

	public Boolean getFBoolean() {
		return fBoolean;
	}

	public void setFBoolean(Boolean boolean1) {
		fBoolean = boolean1;
	}

	public Byte getFByte() {
		return fByte;
	}

	public void setFByte(Byte byte1) {
		fByte = byte1;
	}

	public Character getFCharacter() {
		return fCharacter;
	}

	public void setFCharacter(Character character) {
		fCharacter = character;
	}

	public Double getFDouble() {
		return fDouble;
	}

	public void setFDouble(Double double1) {
		fDouble = double1;
	}

	public Float getFFloat() {
		return fFloat;
	}

	public void setFFloat(Float float1) {
		fFloat = float1;
	}

	public Integer getFInteger() {
		return fInteger;
	}

	public void setFInteger(Integer integer) {
		fInteger = integer;
	}

	public Long getFLong() {
		return fLong;
	}

	public void setFLong(Long long1) {
		fLong = long1;
	}

	public Short getFShort() {
		return fShort;
	}

	public void setFShort(Short short1) {
		fShort = short1;
	}

	public String getFString() {
		return fString;
	}

	public void setFString(String string) {
		fString = string;
	}

	public boolean isFboolean() {
		return fboolean;
	}

	public void setFboolean(boolean fboolean) {
		this.fboolean = fboolean;
	}

	public byte getFbyte() {
		return fbyte;
	}

	public void setFbyte(byte fbyte) {
		this.fbyte = fbyte;
	}

	public char getFchar() {
		return fchar;
	}

	public void setFchar(char fchar) {
		this.fchar = fchar;
	}

	public double getFdouble() {
		return fdouble;
	}

	public void setFdouble(double fdouble) {
		this.fdouble = fdouble;
	}

	public float getFfloat() {
		return ffloat;
	}

	public void setFfloat(float ffloat) {
		this.ffloat = ffloat;
	}

	public int getFint() {
		return fint;
	}

	public void setFint(int fint) {
		this.fint = fint;
	}

	public long getFlong() {
		return flong;
	}

	public void setFlong(long flong) {
		this.flong = flong;
	}

	public short getFshort() {
		return fshort;
	}

	public void setFshort(short fshort) {
		this.fshort = fshort;
	}

	public Object getFObject() {
		return fObject;
	}

	public void setFObject(Object object) {
		fObject = object;
	}

	public static TestEntity createInstrance() {
		TestEntity te = new TestEntity();
		te.setFboolean(true);
		te.setFBoolean(new Boolean(true));
		te.setFbyte((byte) 1);
		te.setFByte(new Byte((byte) 255));
		te.setFchar((char) 'a');
		te.setFCharacter(new Character((char) 'z'));
		te.setFdouble(123456.789D);
		te.setFDouble(new Double(987.123456D));
		te.setFfloat(123.456F);
		te.setFFloat(new Float(123456.789F));
		te.setFint(123456);
		te.setFInteger(new Integer(654321));
		te.setFlong(123456789L);
		te.setFLong(new Long(987654321L));
		te.setFshort((short) 123);
		te.setFShort(new Short((short) 123));
		te.setFString("hello world");

		TestEntityII teII = new TestEntityII();
		teII.setId(1);
		teII.setTitle("titletitletitle");
		te.setFObject(teII);
		return te;
	}
}
