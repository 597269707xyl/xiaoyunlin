package com.zdtech.platform.framework.message.xml;

import com.cfca.toolkit.Castle;
import com.cfca.toolkit.CastleException;
import com.cfca.toolkit.CastleProperties;
import com.cfca.util.pki.cipher.JCrypto;
import com.cfca.util.pki.cipher.Mechanism;
import com.cfca.util.pki.cipher.Session;
import com.cfca.util.pki.pkcs.PKCS12;
import com.cfca.util.pki.pkcs.PKCS7SignedData;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class CnccCastle extends Castle {
	private JCrypto jcrypto = JCrypto.getInstance();
	private Session session = null;
	private String mUserCertFilePath;
	private String mUserCertPassword;
	private X509Certificate mUserCert;
	private PrivateKey mPrivateKey;
	private boolean mUserCertReady;
	private PKCS12 p12;

	public CnccCastle(CastleProperties arg0) {
		super(arg0);
		mUserCertFilePath = arg0.getmUserCertFilePath();
		mUserCertPassword = arg0.getmUserCertPassword();
		try {
			jcrypto.initialize("JSOFT_LIB", null);
			session = jcrypto.openSession("JSOFT_LIB");
		} catch (Exception exception) {
		}
	}

	public void initCertAppContext() throws FileNotFoundException, IOException,
			CertificateException, UnrecoverableKeyException, CastleException 
	{
		if (mUserCertFilePath != null && mUserCertPassword != null) {
			p12 = new PKCS12();
			getPrivateKeyAndCertificate(mUserCertFilePath, mUserCertPassword);
			try {
				p12.load(mUserCertFilePath);
				p12.decrypt(mUserCertPassword.toCharArray());
			} catch (Exception ex) {
				throw new CastleException(ex);
			}
		}
	}
	public void initCertAppContext2() throws FileNotFoundException, IOException,
			CertificateException, UnrecoverableKeyException, CastleException
	{
		if (mUserCertFilePath != null && mUserCertPassword != null) {
			p12 = new PKCS12();
			getPrivateKeyAndCertificate2(mUserCertFilePath, mUserCertPassword);
			try(InputStream is = CnccCastle.class.getResourceAsStream(mUserCertFilePath)) {
				p12.load(is);
				p12.decrypt(mUserCertPassword.toCharArray());
			} catch (Exception ex) {
				throw new CastleException(ex);
			}
		}
	}

	public String SignDataDetached(byte[] srcMessage) throws Exception {
		try {
			byte[] signData = new byte[1672];
			PKCS7SignedData p7 = new PKCS7SignedData(session);
			Mechanism mechanism1 = new Mechanism("SHA1");
			signData = p7.generateSignedData(false, PKCS7SignedData.DATA,
					srcMessage, mechanism1, p12.getPrivateKey(),
					p12.getCerts(), null);
			signData = p7.generateSignedDataContent(signData);
			byte b64[] = com.cfca.util.pki.encoders.Base64.encode(signData);
			signData = convertSignResult(b64);
			return new String(signData);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean VerifySignedDataDetached(String signature, byte[] srcMessage) {
		PKCS7SignedData p7 = new PKCS7SignedData(session);
		boolean verify = false;
		try {
			p7.loadBase64(convert64SignResult(signature.getBytes()));
			verify = p7.verifyP7SignedData(srcMessage);
		} catch (Exception ex) {
			return false;
		}
		return verify;
	}



	private byte[] convert64SignResult(byte[] data) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte tmp;
		while ((tmp = (byte) bis.read()) != -1) {
			if (tmp != 10 && tmp != 13)
				bos.write(tmp);
		}
		byte[] base64Data = new String(bos.toByteArray()).trim().getBytes();
		return base64Data;
	}

	private void getPrivateKeyAndCertificate(String mUserCertFilePath,
			String mUserCertPassword) throws FileNotFoundException,
			IOException, CertificateException, UnrecoverableKeyException,
			CastleException {
		FileInputStream keyStoreStream = null;
		KeyStore keyStore = null;
		try {
			File certFile = new File(mUserCertFilePath);
			String certFileName = certFile.getName();
			char password[] = mUserCertPassword.toCharArray();
			if (certFileName.toLowerCase().indexOf(".pfx") != -1
					|| certFileName.toLowerCase().indexOf(".keystore") != -1
					|| certFileName.toLowerCase().indexOf(".jks") != -1) {
				keyStoreStream = new FileInputStream(certFile);
				if (certFileName.toLowerCase().indexOf(".pfx") != -1)
					keyStore = KeyStore.getInstance("PKCS12", "BC");
				else
					keyStore = KeyStore.getInstance("JKS");
				keyStore.load(keyStoreStream, password);
				Enumeration aliasesEnum = keyStore.aliases();
				do {
					if (!aliasesEnum.hasMoreElements())
						break;
					String alias = (String) aliasesEnum.nextElement();
					if (mPrivateKey == null && keyStore.isKeyEntry(alias))
						mPrivateKey = (PrivateKey) keyStore.getKey(alias,
								password);
					if (mUserCert == null && keyStore.isKeyEntry(alias))
						mUserCert = (X509Certificate) keyStore
								.getCertificate(alias);
				} while (true);
				keyStoreStream.close();
			} else {
				throw new CastleException(
						"\u53EA\u652F\u6301pfx\u6587\u4EF6\u6216keystore jks\u5B58\u50A8\u7528\u6237\u8BC1\u4E66");
			}
		} catch (KeyStoreException kse) {
			throw new CastleException(kse);
		} catch (FileNotFoundException ffe) {
			throw ffe;
		} catch (IOException ioe) {
			throw ioe;
		} catch (NoSuchAlgorithmException nsae) {
			throw new CastleException("not support this algorithm");
		} catch (UnrecoverableKeyException uke) {
			throw uke;
		} catch (NoSuchProviderException nspe) {
			throw new CastleException("jce provider not match"
					+ nspe.getMessage());
		} catch (CertificateException ce) {
			throw ce;
		}
		if (mPrivateKey != null && mUserCert != null)
			mUserCertReady = true;
		else
			throw new CastleException(
					"\u83B7\u53D6\u7528\u6237\u8BC1\u4E66\u53CA\u79C1\u94A5\u51FA\u9519");
	}
	private void getPrivateKeyAndCertificate2(String mUserCertFilePath,
											 String mUserCertPassword) throws FileNotFoundException,
			IOException, CertificateException, UnrecoverableKeyException,
			CastleException {
		//FileInputStream keyStoreStream = null;
		KeyStore keyStore = null;
		try {
			File certFile = new File(mUserCertFilePath);
			String certFileName = mUserCertFilePath;
			char password[] = mUserCertPassword.toCharArray();
			if (certFileName.toLowerCase().indexOf(".pfx") != -1
					|| certFileName.toLowerCase().indexOf(".keystore") != -1
					|| certFileName.toLowerCase().indexOf(".jks") != -1) {
				//keyStoreStream = new FileInputStream(certFile);
				if (certFileName.toLowerCase().indexOf(".pfx") != -1)
					keyStore = KeyStore.getInstance("PKCS12", "BC");
				else
					keyStore = KeyStore.getInstance("JKS");
				try (InputStream is = CnccCastle.class.getResourceAsStream(mUserCertFilePath) ){
					keyStore.load(is, password);
				}
				Enumeration aliasesEnum = keyStore.aliases();
				do {
					if (!aliasesEnum.hasMoreElements())
						break;
					String alias = (String) aliasesEnum.nextElement();
					if (mPrivateKey == null && keyStore.isKeyEntry(alias))
						mPrivateKey = (PrivateKey) keyStore.getKey(alias,
								password);
					if (mUserCert == null && keyStore.isKeyEntry(alias))
						mUserCert = (X509Certificate) keyStore
								.getCertificate(alias);
				} while (true);
				//keyStoreStream.close();
			} else {
				throw new CastleException(
						"\u53EA\u652F\u6301pfx\u6587\u4EF6\u6216keystore jks\u5B58\u50A8\u7528\u6237\u8BC1\u4E66");
			}
		} catch (KeyStoreException kse) {
			throw new CastleException(kse);
		} catch (FileNotFoundException ffe) {
			throw ffe;
		} catch (IOException ioe) {
			throw ioe;
		} catch (NoSuchAlgorithmException nsae) {
			throw new CastleException("not support this algorithm");
		} catch (UnrecoverableKeyException uke) {
			throw uke;
		} catch (NoSuchProviderException nspe) {
			throw new CastleException("jce provider not match"
					+ nspe.getMessage());
		} catch (CertificateException ce) {
			throw ce;
		}
		if (mPrivateKey != null && mUserCert != null)
			mUserCertReady = true;
		else
			throw new CastleException(
					"\u83B7\u53D6\u7528\u6237\u8BC1\u4E66\u53CA\u79C1\u94A5\u51FA\u9519");
	}

	private byte[] convertSignResult(byte data[]) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte tmp;
		while ((tmp = (byte) bis.read()) != -1)
			bos.write(tmp);
		byte base64Data[] = (new String(bos.toByteArray())).trim().getBytes();
		bos = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < base64Data.length; i++) {
				if (i != 0 && i % 64 == 0)
					bos.write("\r\n".getBytes());
				bos.write(base64Data[i]);
			}

			bos.write("\r\n".getBytes());
		} catch (Exception ex) {
			return null;
		}
		return bos.toByteArray();
	}
}
