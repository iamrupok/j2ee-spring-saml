/*
 * @(#) CryptoException.java Aug 6, 2005
 * Copyright 2005 Frequency Marketing, Inc. All rights reserved.
 * Frequency Marketing, Inc. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.frequencymarketing.common.app.security;

/**
 * The crypto exception indicates a problem with the encryption or
 * decryption process.
 * @author Edward Sumerfield
 */
public class CryptoException extends Exception
{
    /**
     * Construct
     * @param a_message
     */
    public CryptoException(String a_message)
    {
        super(a_message);
    }
    
    /**
     * Contruct
     * @param a_message
     * @param a_exception
     */
    public CryptoException(String a_message, Exception a_exception)
    {
        super(a_message, a_exception);
    }
}
