/*
 * ----- BEGIN LICENSE BLOCK -----
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is PharmGen.
 *
 * The Initial Developer of the Original Code is PharmGKB (The Pharmacogenetics
 * and Pharmacogenetics Knowledge Base, supported by NIH U01GM61374). Portions
 * created by the Initial Developer are Copyright (C) 2013 the Initial Developer.
 * All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or the
 * GNU Lesser General Public License Version 2.1 or later (the "LGPL"), in
 * which case the provisions of the GPL or the LGPL are applicable instead of
 * those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ----- END LICENSE BLOCK -----
 */

package org.pharmgkb;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import util.ItpcUtils;
import util.StringPair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Ryan Whaley
 * Date: Jul 13, 2010
 * Time: 11:16:54 PM
 */
public class Genotype extends StringPair {
  public enum Metabolizer {Unknown, PM, IM, EM, UM}
  public static final String EXTENSIVE = "Extensive";
  public static final String INTERMEDIATE = "Intermediate";
  public static final String POOR = "Poor";
  public static final String UNKNOWN = "Unknown";

  private static final Pattern sf_numberPattern = Pattern.compile("\\D*(\\d+)\\D*");
  private static final Logger sf_logger = Logger.getLogger(Genotype.class);

  private static final Map<String,Metabolizer> metabMap = new HashMap<String,Metabolizer>();
  static {
    metabMap.put("*3",Metabolizer.PM);
    metabMap.put("*4",Metabolizer.PM);
    metabMap.put("*5",Metabolizer.PM);
    metabMap.put("*6",Metabolizer.PM);
    metabMap.put("*7",Metabolizer.PM);
    metabMap.put("*8",Metabolizer.PM);
    metabMap.put("*11",Metabolizer.PM);
    metabMap.put("*12",Metabolizer.PM);
    metabMap.put("*13",Metabolizer.PM);
    metabMap.put("*14",Metabolizer.PM);
    metabMap.put("*15",Metabolizer.PM);
    metabMap.put("*16",Metabolizer.PM);
    metabMap.put("*18",Metabolizer.PM);
    metabMap.put("*19",Metabolizer.PM);
    metabMap.put("*20",Metabolizer.PM);
    metabMap.put("*40",Metabolizer.PM);
    metabMap.put("*42",Metabolizer.PM);
    metabMap.put("*44",Metabolizer.PM);
    metabMap.put("*56",Metabolizer.PM);
    metabMap.put("*38",Metabolizer.PM);
    metabMap.put("*4XN",Metabolizer.PM);

    metabMap.put("*9",Metabolizer.IM);
    metabMap.put("*9XN",Metabolizer.IM);
    metabMap.put("*10",Metabolizer.IM);
    metabMap.put("*10XN",Metabolizer.IM);
    metabMap.put("*17",Metabolizer.IM);
    metabMap.put("*29",Metabolizer.IM);
    metabMap.put("*37",Metabolizer.IM);
    metabMap.put("*41",Metabolizer.IM);
    metabMap.put("*41XN",Metabolizer.IM);
    metabMap.put("*45",Metabolizer.IM);
    metabMap.put("*46",Metabolizer.IM);

    metabMap.put("*1",Metabolizer.EM);
    metabMap.put("*2",Metabolizer.EM);
    metabMap.put("*2A",Metabolizer.EM);
    metabMap.put("*33",Metabolizer.EM);
    metabMap.put("*35",Metabolizer.EM);
    metabMap.put("*39",Metabolizer.EM);
    metabMap.put("*43",Metabolizer.EM);

    metabMap.put("*1XN",Metabolizer.UM);
    metabMap.put("*2XN",Metabolizer.UM);
    metabMap.put("*35XN",Metabolizer.UM);
    metabMap.put("*39XN",Metabolizer.UM);

    metabMap.put("Unknown", Metabolizer.Unknown);
  }

  private static final Map<Metabolizer,Float> scoreMap = new HashMap<Metabolizer,Float>();
  static {
    scoreMap.put(Metabolizer.PM, 0f);
    scoreMap.put(Metabolizer.IM, 0.5f);
    scoreMap.put(Metabolizer.EM, 1f);
    scoreMap.put(Metabolizer.UM, 2f);
  }

  private static final Map<Metabolizer,Integer> priorityMap = new HashMap<Metabolizer,Integer>();
  static {
    priorityMap.put(Metabolizer.PM,1); // top priority
    priorityMap.put(Metabolizer.IM,2);
    priorityMap.put(Metabolizer.EM,3);
    priorityMap.put(Metabolizer.UM,4);
    priorityMap.put(Metabolizer.Unknown, 5); // bottom priority
  }

  public Genotype() {}

  public Genotype(String string) {
    if (string != null && string.contains("/")) {
      String[] tokens = string.split("/");
      for (String token : tokens) {
        addString(token);
      }
    }
  }

  public Genotype(String s1, String s2) {
    addString(s1);
    addString(s2);
  }

  public boolean isValid(String string) {
    return metabMap.keySet().contains(ItpcUtils.alleleStrip(string)) || string.equals("Unknown");
  }

  public Float getScore() {
    Float score = null;

    if (!this.isUncertain()) {
      score = 0f;
      for (String allele : this.getStrings()) {
        score += scoreMap.get(metabMap.get(ItpcUtils.alleleStrip(allele)));
      }
    }

    return score;
  }

  /**
   * Adds an allele to the List of alleles, taking into consideration the prioritization found
   * in the genoPriority Map for this class
   * @param string a new String allele to add to the List
   */
  public void addString(String string) {
    if (!isValid(string)) {
      return;
    }

    if (this.getStrings().isEmpty() || this.getStrings().size()==1) {
      super.addString(string);
    }
    else {
      String removeAllele = null;
      for (String existingAllele : this.getStrings()) {
        // if the new allele has a higher priority (lower number) than an existing one, replace it
        if ((priority(string) < priority(existingAllele))
            || (existingAllele.equals("*1") && priority(existingAllele)==(priority(string)))
            ) {
          removeAllele = existingAllele;
        }
      }
      if (!StringUtils.isBlank(removeAllele)) {
        removeString(removeAllele);
        super.addString(string);
      }
    }
    reorder();
  }

  protected void reorder() {
    if (getStrings().size()==2) {

      try {
        Integer int0, int1;
        Matcher matcher = sf_numberPattern.matcher(getStrings().get(0));
        if (matcher.find()) {
          int0 = Integer.parseInt(matcher.group(1));
        }
        else {
          return;
        }

        matcher.reset(getStrings().get(1));
        if (matcher.find()) {
          int1 = Integer.parseInt(matcher.group(1));
        }
        else {
          return;
        }

        if (int0>int1) {
          Collections.reverse(getStrings());
        }
      }
      catch (Exception ex) {
        if (sf_logger.isDebugEnabled()) {
          sf_logger.debug("Error reordering: " + getStrings());
        }
      }

    }
  }

  protected static String getText(Metabolizer value) {
    if (value == null) {
      return "Unknown";
    }
    else {
      switch (value) {
        case IM: return "IM";
        case PM: return "PM";
        case EM: return "EM";
        case UM: return "UM";
        default: return "Unknown";
      }
    }
  }

  public String getMetabolizerStatus() {
    if (this.getStrings().isEmpty()) {
      return getText(Metabolizer.Unknown);
    }

    List<String> genotypes = new ArrayList<String>();
    for (String allele : this.getStrings()) {
      if (allele != null && !allele.equals("Unknown") && !metabMap.keySet().contains(ItpcUtils.alleleStrip(allele))) {
        sf_logger.warn("Can't find map for allele: " + ItpcUtils.alleleStrip(allele));
      }
      genotypes.add(getText(metabMap.get(ItpcUtils.alleleStrip(allele))));
    }

    StringBuilder genoBuilder = new StringBuilder();
    Collections.sort(genotypes, String.CASE_INSENSITIVE_ORDER);
    for (int x = 0; x < genotypes.size(); x++) {
      genoBuilder.append(genotypes.get(x));
      if (x != genotypes.size() - 1) {
        genoBuilder.append("/");
      }
    }
    return genoBuilder.toString();
  }
  
  public String getMetabolizerGroup() {
    String group = "Unknown";

    // modify genoMetabStatusIdx description field if this changes
    if (getMetabolizerStatus().equals("EM/EM")
            || getMetabolizerStatus().equals("EM/UM")
            || getMetabolizerStatus().equals("IM/UM")
            || getMetabolizerStatus().equals("UM/UM")) {
      group = "Extensive";
    }
    else if (getMetabolizerStatus().equals("EM/PM")
            || getMetabolizerStatus().equals("IM/IM")
            || getMetabolizerStatus().equals("IM/PM")
            || getMetabolizerStatus().equals("PM/UM")
            || getMetabolizerStatus().equals("EM/IM")) {
      group = "Intermediate";
    }
    else if (getMetabolizerStatus().equals("PM/PM")) {
      group = "Poor";
    }

    return group;
  }

  public boolean isHeteroDeletion() {
    return this.getStrings().contains("*5");
  }

  public boolean isHomoDeletion() {
    return this.is("*5","*5");
  }

  private float priority(String allele) {
    return priorityMap.get(metabMap.get(ItpcUtils.alleleStrip(allele)));
  }

  public boolean is(Metabolizer status1, Metabolizer status2) {
    if (status1 != null && status2 != null) {
      if (status1 == status2) {
        return count(status1)==2;
      }
      else {
        return (count(status1) == 1 && count(status2)==1);
      }
    }
    return false;
  }

  protected int count(Metabolizer status) {
    int count = 0;
    for (String element : getStrings()) {
      if (metabMap.get(ItpcUtils.alleleStrip(element)) == status) count++;
    }
    return count;
  }

  public boolean isUnknown() {
    return contains("Unknown");
  }
}
