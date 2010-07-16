/**
 * Created by IntelliJ IDEA.
 * User: whaleyr
 * Date: Jul 14, 2010
 * Time: 8:16:01 AM
 */
public class Subject {
  public static final String PAROXETINE = "Paroxetine";
  public static final String FLUOXETINE = "Fluoxetine";
  public static final String QUINIDINE = "Quinidine";
  public static final String BUPROPION = "Buproprion";
  public static final String DULOXETINE = "Duloxetine";
  public static final String CIMETIDINE = "Cimetidine";
  public static final String SERTRALINE = "Sertraline";
  public static final String CITALOPRAM = "Citalopram";

  private String m_subjectId = null;
  private String m_projectSite = null;
  private String m_age = null;
  private String m_menoStatus = null;
  private String m_metastatic = null;
  private String m_erStatus = null;
  private String m_duration = null;
  private String m_tamoxDose = null;
  private String m_tumorSource = null;
  private String m_bloodSource = null;
  private String m_priorHistory = null;
  private String m_priorSites = null;
  private String m_priorDcis = null;
  private String m_chemo = null;
  private String m_hormone = null;
  private String m_systemicTher = null;
  private String m_followup = null;
  private String m_timeBtwSurgTamox = null;
  private String m_firstAdjEndoTher = null;
  private String m_genoSource1 = null;
  private String m_genoSource2 = null;
  private String m_genoSource3 = null;
  private Deletion m_deletion = null;

  private Value m_hasParoxetine = Value.Unknown;
  private Value m_hasFluoxetine = Value.Unknown;
  private Value m_hasQuinidine = Value.Unknown;
  private Value m_hasBuproprion = Value.Unknown;
  private Value m_hasDuloxetine = Value.Unknown;

  private Value m_hasCimetidine = Value.Unknown;
  private Value m_hasSertraline = Value.Unknown;
  private Value m_hasCitalopram = Value.Unknown;

  private Genotype m_genotypePgkb = new Genotype();
  private Genotype m_genotypeAmplichip = new Genotype();
  private Genotype m_genotypeFinal = new Genotype();

  private VariantAlleles m_rs1065852 = new VariantAlleles();
  private VariantAlleles m_rs4986774 = new VariantAlleles();
  private VariantAlleles m_rs3892097 = new VariantAlleles();
  private VariantAlleles m_rs5030655 = new VariantAlleles();
  private VariantAlleles m_rs16947 = new VariantAlleles();
  private VariantAlleles m_rs28371706 = new VariantAlleles();
  private VariantAlleles m_rs28371725 = new VariantAlleles();

  public Genotype getGenotypePgkb() {
    return m_genotypePgkb;
  }

  protected void setGenotypePgkb(Genotype genotypePgkb) {
    m_genotypePgkb = genotypePgkb;
  }

  public Genotype getGenotypeAmplichip() {
    return m_genotypeAmplichip;
  }

  public void setGenotypeAmplichip(Genotype genotypeAmplichip) {
    m_genotypeAmplichip = genotypeAmplichip;
  }

  public Genotype getGenotypeFinal() {
    return m_genotypeFinal;
  }

  public void setGenotypeFinal(Genotype genotypeFinal) {
    m_genotypeFinal = genotypeFinal;
  }

  public Value getWeak() {
    if (this.hasCimetidine() == Value.Yes
        || this.hasSertraline() == Value.Yes
        || this.hasCitalopram() == Value.Yes) {
      return Value.Yes;
    }
    else if (this.hasCimetidine() == Value.No
        && this.hasSertraline() == Value.No
        && this.hasCitalopram() == Value.No) {
      return Value.No;
    }
    else {
      return Value.Unknown;
    }
  }

  public Value getPotent() {
    if (this.hasParoxetine() == Value.Yes
        || this.hasFluoxetine() == Value.Yes
        || this.hasQuinidine() == Value.Yes
        || this.hasBuproprion() == Value.Yes
        || this.hasDuloxetine() == Value.Yes) {
      return Value.Yes;
    }
    else if (this.hasParoxetine() == Value.No
        && this.hasFluoxetine() == Value.No
        && this.hasQuinidine() == Value.No
        && this.hasBuproprion() == Value.No
        && this.hasDuloxetine() == Value.No) {
      return Value.No;
    }
    else {
      return Value.Unknown;
    }
  }

  public VariantAlleles getRs1065852() {
    return m_rs1065852;
  }

  public void setRs1065852(VariantAlleles rs1065852) {
    m_rs1065852 = rs1065852;
    this.calculateGenotypePgkb();
  }

  public VariantAlleles getRs4986774() {
    return m_rs4986774;
  }

  public void setRs4986774(VariantAlleles rs4986774) {
    m_rs4986774 = rs4986774;
    this.calculateGenotypePgkb();
  }

  public VariantAlleles getRs3892097() {
    return m_rs3892097;
  }

  public void setRs3892097(VariantAlleles rs3892097) {
    m_rs3892097 = rs3892097;
    this.calculateGenotypePgkb();
  }

  public VariantAlleles getRs5030655() {
    return m_rs5030655;
  }

  public void setRs5030655(VariantAlleles rs5030655) {
    m_rs5030655 = rs5030655;
    this.calculateGenotypePgkb();
  }

  public VariantAlleles getRs16947() {
    return m_rs16947;
  }

  public void setRs16947(VariantAlleles rs16947) {
    m_rs16947 = rs16947;
    this.calculateGenotypePgkb();
  }

  public VariantAlleles getRs28371706() {
    return m_rs28371706;
  }

  public void setRs28371706(VariantAlleles rs28371706) {
    m_rs28371706 = rs28371706;
    this.calculateGenotypePgkb();
  }

  public VariantAlleles getRs28371725() {
    return m_rs28371725;
  }

  public void setRs28371725(VariantAlleles rs28371725) {
    m_rs28371725 = rs28371725;
    this.calculateGenotypePgkb();
  }

  public Float getScore() {
    Float score = null;

    if (!this.getGenotypeFinal().isUncertain() && this.getWeak() != Value.Unknown) {
      Float genoScore = this.getGenotypeFinal().getScore();
      Float weakPenalty = this.getWeak()==Value.Yes ? -0.5f : 0f;

      score = genoScore + weakPenalty;

      if (score<0f) {score=0f;}
    }

    return score;
  }

  public Value hasParoxetine() {
    return m_hasParoxetine;
  }

  public void setHasParoxetine(Value hasParoxetine) {
    m_hasParoxetine = hasParoxetine;
  }

  public Value hasFluoxetine() {
    return m_hasFluoxetine;
  }

  public void setHasFluoxetine(Value hasFluoxetine) {
    m_hasFluoxetine = hasFluoxetine;
  }

  public Value hasQuinidine() {
    return m_hasQuinidine;
  }

  public void setHasQuinidine(Value hasQuinidine) {
    m_hasQuinidine = hasQuinidine;
  }

  public Value hasBuproprion() {
    return m_hasBuproprion;
  }

  public void setHasBuproprion(Value hasBuproprion) {
    m_hasBuproprion = hasBuproprion;
  }

  public Value hasDuloxetine() {
    return m_hasDuloxetine;
  }

  public void setHasDuloxetine(Value hasDuloxetine) {
    m_hasDuloxetine = hasDuloxetine;
  }

  public Value hasCimetidine() {
    return m_hasCimetidine;
  }

  public void setHasCimetidine(Value hasCimetidine) {
    m_hasCimetidine = hasCimetidine;
  }

  public Value hasSertraline() {
    return m_hasSertraline;
  }

  public void setHasSertraline(Value hasSertraline) {
    m_hasSertraline = hasSertraline;
  }

  public Value hasCitalopram() {
    return m_hasCitalopram;
  }

  public void setHasCitalopram(Value hasCitalopram) {
    m_hasCitalopram = hasCitalopram;
  }

  public boolean deletionDetectable() {
    return this.getDeletion()!=Deletion.Unknown
        || this.getRs4986774().is("-","a")
        || this.getRs1065852().is("c","t")
        || this.getRs3892097().is("a","g")
        || this.getRs5030655().is("-","t")
        || this.getRs16947().is("c","t")
        || this.getRs28371706().is("c","t")
        || this.getRs28371725().is("g","a");
  }

  public void calculateGenotypePgkb() {
    Genotype geno = new Genotype();

    switch (this.getDeletion()) {
      case Hetero: geno.addString("*5"); break;
      case Homo: geno = new Genotype("*5/*5"); break;
    }

    if (!geno.isHomoDeletion()) {
      // *3
      if (this.getRs4986774().hasData()) {
        if (geno.isHeteroDeletion() && this.getRs4986774().count("-")==2) {
          geno.addString("*3");
        }
        else if (!geno.isHeteroDeletion()) {
          for (int i=0; i<this.getRs4986774().count("-"); i++) {
            geno.addString("*3");
          }
        }
      }

      // *6
      if (this.getRs5030655().hasData()) {
        if (geno.isHeteroDeletion() && this.getRs5030655().count("-")==2) {
          geno.addString("*6");
        }
        else if (!geno.isHeteroDeletion()) {
          for (int i=0; i<this.getRs5030655().count("-"); i++) {
            geno.addString("*6");
          }
        }
      }

      // *4
      if (this.getRs3892097().hasData()) {
        if (this.getRs3892097().contains("a")) {
          geno.addString("*4");
        }
        if (this.getRs3892097().count("a")==2 && !geno.isHeteroDeletion()) {
          geno.addString("*4");
        }
      }

      // *41
      if (this.getRs28371725().hasData()) {
        if (this.getRs28371725().contains("a")) {
          geno.addString("*41");
        }
        if (this.getRs28371725().count("a")==2 && deletionDetectable() && !geno.isHeteroDeletion()) {
          geno.addString("*41");
        }
      }

      //       *4                    *3                   *6 w/ exception for a haplotype rule for *10
      if (this.getRs3892097().hasData() && this.getRs4986774().hasData() && (this.getRs5030655().hasData() || (this.getRs1065852().is("t","c") && !this.getRs5030655().hasData())) && deletionDetectable()) {

        // *2
        if (this.getRs16947().hasData() && this.getRs28371706().count("c")>0 && this.getRs28371725().count("g")>0 && this.getRs1065852().hasData()) {
          if (this.getRs16947().count("t")>0 && this.getRs28371706().count("t")<2 && this.getRs28371725().count("a")<2) {
            geno.addString("*2");
            if (!geno.isHeteroDeletion() &&
                (this.getRs16947().is("t","t") && this.getRs28371706().is("c","c") && this.getRs28371725().is("g","g"))) {
              geno.addString("*2");
            }
          }
        }

        // *10
        if (this.getRs1065852().hasData()) {
          if (this.getRs1065852().contains("t") && this.getRs3892097().count("a")==0) {
            geno.addString("*10");
          }
          if (!geno.isHeteroDeletion() && this.getRs1065852().is("t","t") && this.getRs3892097().contains("g")) {
            geno.addString("*10");
          }
        }

        // *17
        if (this.getRs28371706().hasData() && this.getRs16947().hasData()) {
          if (this.getRs28371706().contains("t") && this.getRs16947().contains("t")) {
            geno.addString("*17");
          }
          if (this.getRs28371706().count("t")==2 && this.getRs16947().count("t")==2
              && deletionDetectable() && !geno.isHeteroDeletion()) {
            geno.addString("*17");
          }
        }
      }
      else if (!deletionDetectable()) { //what to do if we don't have complete *5 knowledge
        if (this.getRs4986774().hasData() &&
            this.getRs1065852().hasData() &&
            this.getRs3892097().hasData() &&
            this.getRs5030655().hasData() &&
            this.getRs16947().is("t","t")   &&
            this.getRs28371725().is("g","g") &&
            this.getRs28371706().is("c","c")) {
          geno.addString("*2");
        }
        else if (this.getRs4986774().hasData() &&
            this.getRs1065852().hasData() &&
            this.getRs3892097().hasData() &&
            this.getRs5030655().hasData() &&
            this.getRs16947().hasData()   &&
            this.getRs28371725().hasData() &&
            this.getRs28371706().hasData() &&
            geno.isEmpty()) {
          geno.addString("*1");
          geno.addString("Unknown");
        }
      }
    }

    if (this.getRs4986774().hasData() && // special case for partial unknown info., from Joan
        this.getRs1065852().hasData() &&
        this.getRs3892097().hasData() &&
        this.getRs5030655().hasData() &&
        this.getRs16947().is("c","t") &&
        (!this.getRs28371706().hasData() || !this.getRs28371725().hasData()) &&
        deletionDetectable() &&
        geno.isEmpty()) {
      geno.addString("*1");
      geno.addString("Unknown");
    }


    while (geno.size() < 2) {
      if (this.getRs4986774().hasData() &&
          this.getRs1065852().hasData() &&
          this.getRs3892097().hasData() &&
          this.getRs5030655().hasData() &&
          this.getRs16947().hasData()   &&
          deletionDetectable() &&
          (this.getRs28371725().hasData() || (!this.getRs28371725().hasData() && (this.getRs16947().is("C","C") || this.getRs16947().is("C","-")))) &&
          (this.getRs28371706().hasData() || (!this.getRs28371706().hasData() && (this.getRs16947().is("C","C") || this.getRs16947().is("C","-")))) &&
          (!geno.isHeteroDeletion() || (geno.isHeteroDeletion() && (this.getRs28371725().hasData() || this.getRs28371706().hasData())))) // we can use rs16947 to exclude *41 calls so it doesn't always have to be available
      {
        geno.addString("*1");
      }
      else {
        geno.addString("Unknown");
      }
    }

    this.setGenotypePgkb(geno);
  }

  public Deletion getDeletion() {
    return m_deletion;
  }

  public void setDeletion(Deletion deletion) {
    m_deletion = deletion;
    this.calculateGenotypePgkb();
  }

  public void setDeletion(String deletion) {
    if (deletion.equalsIgnoreCase("homozygous deletion")) {
      this.setDeletion(Deletion.Homo);
    }
    else if (deletion.contains("deletion") && !deletion.contains("no deletion")) {
      this.setDeletion(Deletion.Hetero);
    }
    else if (deletion.equalsIgnoreCase("NA")) {
      this.setDeletion(Deletion.Unknown);
    }
    else {
      this.setDeletion(Deletion.None);
    }

  }

  enum Value {Unknown, Yes, No}
  enum Deletion {Unknown, None, Hetero, Homo}
}