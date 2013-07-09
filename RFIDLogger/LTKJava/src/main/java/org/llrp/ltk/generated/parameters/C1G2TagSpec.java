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
import org.llrp.ltk.generated.interfaces.AirProtocolTagSpec;
import org.llrp.ltk.generated.parameters.C1G2TargetTag;
import org.llrp.ltk.types.LLRPBitList;
import org.llrp.ltk.types.LLRPMessage;
import org.llrp.ltk.types.SignedShort;
import org.llrp.ltk.types.TLVParameter;
import org.llrp.ltk.types.TVParameter;
import org.llrp.ltk.types.UnsignedShort;

import java.util.LinkedList;
import java.util.List;


/**
 * This parameter describes the target tag population on which certain operations have to be performed.  This Parameter is similar to the selection C1G2Filter Parameter described earlier. However, because these tags are stored in the Reader's memory and ternary comparisons are to be allowed for, each bit i in the target tag is represented using 2 bits - bit i in mask, and bit i in tag pattern.  If bit i in the mask is zero, then bit i of the target tag is a don't care (X); if bit i in the mask is one, then bit i of the target tag is bit i of the tag pattern. For example, "all tags" is specified using a mask length of zero.This parameter can carry up to two tag patterns. If more than one pattern is present, a Boolean AND is implied. Each tag pattern has a match or a non-match flag, allowing (A and B,!A and B, !A and !B, A and !B), where A and B are the tag patterns.The tagSpec contains 2 tag patterns.

See also {@link <a href="http://www.epcglobalinc.org/standards/llrp/llrp_1_0_1-standard-20070813.pdf#page=106&view=fit">LLRP Specification Section 15.2.1.3.1</a>}
 and {@link <a href="http://www.epcglobalinc.org/standards/llrp/llrp_1_0_1-standard-20070813.pdf#page=156&view=fit">LLRP Specification Section 16.3.1.3.1</a>}


 */

/**
 * This parameter describes the target tag population on which certain operations have to be performed.  This Parameter is similar to the selection C1G2Filter Parameter described earlier. However, because these tags are stored in the Reader's memory and ternary comparisons are to be allowed for, each bit i in the target tag is represented using 2 bits - bit i in mask, and bit i in tag pattern.  If bit i in the mask is zero, then bit i of the target tag is a don't care (X); if bit i in the mask is one, then bit i of the target tag is bit i of the tag pattern. For example, "all tags" is specified using a mask length of zero.This parameter can carry up to two tag patterns. If more than one pattern is present, a Boolean AND is implied. Each tag pattern has a match or a non-match flag, allowing (A and B,!A and B, !A and !B, A and !B), where A and B are the tag patterns.The tagSpec contains 2 tag patterns.

See also {@link <a href="http://www.epcglobalinc.org/standards/llrp/llrp_1_0_1-standard-20070813.pdf#page=106&view=fit">LLRP Specification Section 15.2.1.3.1</a>}
 and {@link <a href="http://www.epcglobalinc.org/standards/llrp/llrp_1_0_1-standard-20070813.pdf#page=156&view=fit">LLRP Specification Section 16.3.1.3.1</a>}

      .
 */
public class C1G2TagSpec extends TLVParameter implements AirProtocolTagSpec {
    public static final SignedShort TYPENUM = new SignedShort(338);
    private static final Logger LOGGER = Logger.getLogger(C1G2TagSpec.class);
    protected List<C1G2TargetTag> c1G2TargetTagList = new LinkedList<C1G2TargetTag>();

    /**
     * empty constructor to create new parameter.
     */
    public C1G2TagSpec() {
    }

    /**
     * Constructor to create parameter from binary encoded parameter
     * calls decodeBinary to decode parameter.
     * @param list to be decoded
     */
    public C1G2TagSpec(LLRPBitList list) {
        decodeBinary(list);
    }

    /**
    * Constructor to create parameter from xml encoded parameter
    * calls decodeXML to decode parameter.
    * @param element to be decoded
    */
    public C1G2TagSpec(Element element) throws InvalidLLRPMessageException {
        decodeXML(element);
    }

    /**
    * {@inheritDoc}
    */
    public LLRPBitList encodeBinarySpecific() {
        LLRPBitList resultBits = new LLRPBitList();

        if (c1G2TargetTagList == null) {
            LOGGER.warn(" c1G2TargetTagList not set");

            //parameter has to be set - throw exception
            throw new MissingParameterException(" c1G2TargetTagList not set");
        } else {
            for (C1G2TargetTag field : c1G2TargetTagList) {
                resultBits.append(field.encodeBinary());
            }
        }

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

        //parameters
        if (c1G2TargetTagList == null) {
            LOGGER.warn(" c1G2TargetTagList not set");
            throw new MissingParameterException("  c1G2TargetTagList not set");
        }

        for (C1G2TargetTag field : c1G2TargetTagList) {
            element.addContent(field.encodeXML(field.getClass().getName()
                                                    .replaceAll(field.getClass()
                                                                     .getPackage()
                                                                     .getName() +
                        ".", ""), ns));
        }

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

        // list of parameters
        c1G2TargetTagList = new LinkedList<C1G2TargetTag>();
        LOGGER.debug("decoding parameter c1G2TargetTagList ");

        while (position < binary.length()) {
            // store if one parameter matched
            boolean atLeastOnce = false;

            // look ahead to see type
            if (binary.get(position)) {
                // do not take the first bit as it is always 1
                type = new SignedShort(binary.subList(position + 1, 7));
            } else {
                type = new SignedShort(binary.subList(position +
                            RESERVEDLENGTH, TYPENUMBERLENGTH));
                tempByteLength = new UnsignedShort(binary.subList(position +
                            RESERVEDLENGTH + TYPENUMBERLENGTH,
                            UnsignedShort.length())).toShort();
                tempLength = 8 * tempByteLength;
            }

            //add parameter to list if type number matches
            if ((type != null) && type.equals(C1G2TargetTag.TYPENUM)) {
                if (binary.get(position)) {
                    // length can statically be determined for TV Parameters
                    tempLength = C1G2TargetTag.length();
                }

                c1G2TargetTagList.add(new C1G2TargetTag(binary.subList(
                            position, tempLength)));
                LOGGER.debug("adding C1G2TargetTag to c1G2TargetTagList ");
                atLeastOnce = true;
                position += tempLength;
            }

            if (!atLeastOnce) {
                //no parameter matched therefore we jump out of the loop
                break;
            }
        }

        //if list is still empty no parameter matched
        if (c1G2TargetTagList.isEmpty()) {
            LOGGER.warn(
                "encoded message does not contain parameter for non optional c1G2TargetTagList");
            throw new MissingParameterException(
                "C1G2TagSpec misses non optional parameter of type C1G2TargetTag");
        }
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

        //parameter - not choices - no special actions needed
        //we expect a list of parameters
        c1G2TargetTagList = new LinkedList<C1G2TargetTag>();
        tempList = element.getChildren("C1G2TargetTag", ns);

        if ((tempList == null) || tempList.isEmpty()) {
            LOGGER.warn(
                "C1G2TagSpec misses non optional parameter of type c1G2TargetTagList");
            throw new MissingParameterException(
                "C1G2TagSpec misses non optional parameter of type c1G2TargetTagList");
        } else {
            for (Element e : tempList) {
                c1G2TargetTagList.add(new C1G2TargetTag(e));
                LOGGER.debug("adding C1G2TargetTag to c1G2TargetTagList ");
            }
        }

        element.removeChildren("C1G2TargetTag", ns);

        if (element.getChildren().size() > 0) {
            String message = "C1G2TagSpec has unknown element " +
                ((Element) element.getChildren().get(0)).getName();
            throw new InvalidLLRPMessageException(message);
        }
    }

    //setters

    /**
    * set c1G2TargetTagList of type  List &lt;C1G2TargetTag>.
    * @param  c1G2TargetTagList to be set
    */
    public void setC1G2TargetTagList(
        final List<C1G2TargetTag> c1G2TargetTagList) {
        this.c1G2TargetTagList = c1G2TargetTagList;
    }

    // end setter

    //getters

    /**
    * get c1G2TargetTagList of type List &lt;C1G2TargetTag> .
    * @return  List &lt;C1G2TargetTag>
    */
    public List<C1G2TargetTag> getC1G2TargetTagList() {
        return c1G2TargetTagList;
    }

    // end getters

    //add methods

    /**
    * add element c1G2TargetTag of type C1G2TargetTag .
    * @param  c1G2TargetTag of type C1G2TargetTag
    */
    public void addToC1G2TargetTagList(C1G2TargetTag c1G2TargetTag) {
        if (this.c1G2TargetTagList == null) {
            this.c1G2TargetTagList = new LinkedList<C1G2TargetTag>();
        }

        this.c1G2TargetTagList.add(c1G2TargetTag);
    }

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
        return "C1G2TagSpec";
    }

    /**
    * return string representation. All field values but no parameters are included
    * @return String
    */
    public String toString() {
        String result = "C1G2TagSpec: ";
        result = result.replaceFirst(", ", "");

        return result;
    }
}