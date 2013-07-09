/*
 *
 * This file was generated by LLRP Code Generator
 * see http://llrp-toolkit.cvs.sourceforge.net/llrp-toolkit/
 * for more information
 * Generated on: Wed Jun 26 17:35:19 PDT 2013;
 *
 */

/*
 * Copyright 2007 ETH Zurich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package org.llrp.ltk.generated.parameters;

import org.apache.log4j.Logger;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import org.llrp.ltk.exceptions.InvalidLLRPMessageException;
import org.llrp.ltk.exceptions.MissingParameterException;
import org.llrp.ltk.generated.LLRPConstants;
import org.llrp.ltk.generated.enumerations.C1G2BlockWriteResultType;
import org.llrp.ltk.generated.interfaces.AccessCommandOpSpecResult;
import org.llrp.ltk.types.LLRPBitList;
import org.llrp.ltk.types.LLRPMessage;
import org.llrp.ltk.types.SignedShort;
import org.llrp.ltk.types.TLVParameter;
import org.llrp.ltk.types.TVParameter;
import org.llrp.ltk.types.UnsignedShort;

import java.util.LinkedList;
import java.util.List;


/**
 * Contains the result of a block write operationReaders that do not support C1G2 Block Write SHALL set CanSupportBlockWrite to false in C1G2LLRPCapabilities. If such a Reader receives an ADD_ACCESSSPEC with an AccessSpec that contains this OpSpec parameter, the Reader SHALL return an error for that message and not add the AccessSpec.

See also {@link <a href="http://www.epcglobalinc.org/standards/llrp/llrp_1_0_1-standard-20070813.pdf#page=113&view=fit">LLRP Specification Section 15.2.1.5.5.6</a>}
 and {@link <a href="http://www.epcglobalinc.org/standards/llrp/llrp_1_0_1-standard-20070813.pdf#page=161&view=fit">LLRP Specification Section 16.3.1.5.5.6</a>}


 */

/**
 * Contains the result of a block write operationReaders that do not support C1G2 Block Write SHALL set CanSupportBlockWrite to false in C1G2LLRPCapabilities. If such a Reader receives an ADD_ACCESSSPEC with an AccessSpec that contains this OpSpec parameter, the Reader SHALL return an error for that message and not add the AccessSpec.

See also {@link <a href="http://www.epcglobalinc.org/standards/llrp/llrp_1_0_1-standard-20070813.pdf#page=113&view=fit">LLRP Specification Section 15.2.1.5.5.6</a>}
 and {@link <a href="http://www.epcglobalinc.org/standards/llrp/llrp_1_0_1-standard-20070813.pdf#page=161&view=fit">LLRP Specification Section 16.3.1.5.5.6</a>}

      .
 */
public class C1G2BlockWriteOpSpecResult extends TLVParameter
    implements AccessCommandOpSpecResult {
    public static final SignedShort TYPENUM = new SignedShort(354);
    private static final Logger LOGGER = Logger.getLogger(C1G2BlockWriteOpSpecResult.class);
    protected C1G2BlockWriteResultType result;
    protected UnsignedShort opSpecID;
    protected UnsignedShort numWordsWritten;

    /**
     * empty constructor to create new parameter.
     */
    public C1G2BlockWriteOpSpecResult() {
    }

    /**
     * Constructor to create parameter from binary encoded parameter
     * calls decodeBinary to decode parameter.
     * @param list to be decoded
     */
    public C1G2BlockWriteOpSpecResult(LLRPBitList list) {
        decodeBinary(list);
    }

    /**
    * Constructor to create parameter from xml encoded parameter
    * calls decodeXML to decode parameter.
    * @param element to be decoded
    */
    public C1G2BlockWriteOpSpecResult(Element element)
        throws InvalidLLRPMessageException {
        decodeXML(element);
    }

    /**
    * {@inheritDoc}
    */
    public LLRPBitList encodeBinarySpecific() {
        LLRPBitList resultBits = new LLRPBitList();

        if (result == null) {
            LOGGER.warn(" result not set");
            throw new MissingParameterException(
                " result not set  for Parameter of Type C1G2BlockWriteOpSpecResult");
        }

        resultBits.append(result.encodeBinary());

        if (opSpecID == null) {
            LOGGER.warn(" opSpecID not set");
            throw new MissingParameterException(
                " opSpecID not set  for Parameter of Type C1G2BlockWriteOpSpecResult");
        }

        resultBits.append(opSpecID.encodeBinary());

        if (numWordsWritten == null) {
            LOGGER.warn(" numWordsWritten not set");
            throw new MissingParameterException(
                " numWordsWritten not set  for Parameter of Type C1G2BlockWriteOpSpecResult");
        }

        resultBits.append(numWordsWritten.encodeBinary());

        return resultBits;
    }

    /**
    * {@inheritDoc}
    */
    public Content encodeXML(String name, Namespace ns) {
        // element in namespace defined by parent element
        Element element = new Element(name, ns);
        // child element are always in default LLRP namespace
        ns = Namespace.getNamespace("llrp", LLRPConstants.LLRPNAMESPACE);

        if (result == null) {
            LOGGER.warn(" result not set");
            throw new MissingParameterException(" result not set");
        } else {
            element.addContent(result.encodeXML("Result", ns));
        }

        if (opSpecID == null) {
            LOGGER.warn(" opSpecID not set");
            throw new MissingParameterException(" opSpecID not set");
        } else {
            element.addContent(opSpecID.encodeXML("OpSpecID", ns));
        }

        if (numWordsWritten == null) {
            LOGGER.warn(" numWordsWritten not set");
            throw new MissingParameterException(" numWordsWritten not set");
        } else {
            element.addContent(numWordsWritten.encodeXML("NumWordsWritten", ns));
        }

        //parameters
        return element;
    }

    /**
    * {@inheritDoc}
    */
    protected void decodeBinarySpecific(LLRPBitList binary) {
        int position = 0;
        int tempByteLength;
        int tempLength = 0;
        int count;
        SignedShort type;
        int fieldCount;
        Custom custom;
        result = new C1G2BlockWriteResultType(binary.subList(position,
                    C1G2BlockWriteResultType.length()));
        position += C1G2BlockWriteResultType.length();
        opSpecID = new UnsignedShort(binary.subList(position,
                    UnsignedShort.length()));
        position += UnsignedShort.length();
        numWordsWritten = new UnsignedShort(binary.subList(position,
                    UnsignedShort.length()));
        position += UnsignedShort.length();
    }

    /**
    * {@inheritDoc}
    */
    public void decodeXML(Element element) throws InvalidLLRPMessageException {
        List<Element> tempList = null;
        boolean atLeastOnce = false;
        Custom custom;

        Element temp = null;

        // child element are always in default LLRP namespace
        Namespace ns = Namespace.getNamespace(LLRPConstants.LLRPNAMESPACE);

        temp = element.getChild("Result", ns);

        if (temp != null) {
            result = new C1G2BlockWriteResultType(temp);
        }

        element.removeChild("Result", ns);
        temp = element.getChild("OpSpecID", ns);

        if (temp != null) {
            opSpecID = new UnsignedShort(temp);
        }

        element.removeChild("OpSpecID", ns);
        temp = element.getChild("NumWordsWritten", ns);

        if (temp != null) {
            numWordsWritten = new UnsignedShort(temp);
        }

        element.removeChild("NumWordsWritten", ns);

        if (element.getChildren().size() > 0) {
            String message = "C1G2BlockWriteOpSpecResult has unknown element " +
                ((Element) element.getChildren().get(0)).getName();
            throw new InvalidLLRPMessageException(message);
        }
    }

    //setters
    /**
    * set result of type C1G2BlockWriteResultType .
    * @param  result to be set
    */
    public void setResult(final C1G2BlockWriteResultType result) {
        this.result = result;
    }

    /**
    * set   opSpecID of type UnsignedShort .
    * @param   opSpecID to be set
    */
    public void setOpSpecID(final UnsignedShort opSpecID) {
        this.opSpecID = opSpecID;
    }

    /**
    * set   numWordsWritten of type UnsignedShort .
    * @param   numWordsWritten to be set
    */
    public void setNumWordsWritten(final UnsignedShort numWordsWritten) {
        this.numWordsWritten = numWordsWritten;
    }

    // end setter

    //getters
    /**
    * get result of type C1G2BlockWriteResultType.
    * @return  C1G2BlockWriteResultType
    */
    public C1G2BlockWriteResultType getResult() {
        return result;
    }

    /**
    * get   opSpecID of type UnsignedShort.
    * @return   type UnsignedShort to be set
    */
    public UnsignedShort getOpSpecID() {
        return this.opSpecID;
    }

    /**
    * get   numWordsWritten of type UnsignedShort.
    * @return   type UnsignedShort to be set
    */
    public UnsignedShort getNumWordsWritten() {
        return this.numWordsWritten;
    }

    // end getters

    //add methods

    // end add

    /**
    * For TLV Parameter length can not be determined at compile time. This method therefore always returns 0.
    * @return Integer always zero
    */
    public static Integer length() {
        return 0;
    }

    /**
    * {@inheritDoc}
    */
    public SignedShort getTypeNum() {
        return TYPENUM;
    }

    /**
    * {@inheritDoc}
    */
    public String getName() {
        return "C1G2BlockWriteOpSpecResult";
    }

    /**
    * return string representation. All field values but no parameters are included
    * @return String
    */
    public String toString() {
        String result = "C1G2BlockWriteOpSpecResult: ";
        result += ", result: ";
        result += result;
        result += ", opSpecID: ";
        result += opSpecID;
        result += ", numWordsWritten: ";
        result += numWordsWritten;
        result = result.replaceFirst(", ", "");

        return result;
    }
}