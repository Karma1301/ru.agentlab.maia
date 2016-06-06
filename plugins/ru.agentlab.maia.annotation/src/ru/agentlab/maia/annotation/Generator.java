package ru.agentlab.maia.annotation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.semanticweb.owlapi.model.AxiomType;

import com.google.common.collect.ImmutableList;

public class Generator {

	static List<String> aBoxPrefixes = ImmutableList.of(
		// @formatter:off
		"Goal",
		"Failed",
		"Added",
		"Removed"
		// @formatter:on
	);

	static List<String> allPrefixes = ImmutableList.of(
		// @formatter:off
		"Have"
		// @formatter:on
	);

	static List<String> names = ImmutableList.of(
		// @formatter:off
		"Class",
		"Property",
		"Individual",
		"Type",
		"PropertyValue",
		"EquivalentClass",
		"SubClassOf",
		"EquivalentProperty",
		"SubPropertyOf",
		"InverseOf",
		"ObjectProperty",
		"DataProperty",
		"Functional",
		"InverseFunctional",
		"Transitive",
		"Symmetric",
		"Reflexive",
		"Irreflexive",
		"SameAs",
		"DisjointWith",
		"DifferentFrom",
		"ComplementOf",
		"Annotation"
		// @formatter:on
	);

	static List<String> extra = ImmutableList.of(
		// @formatter:off
		"StrictSubClassOf",
		"DirectSubClassOf",
		"DirectType",
		"StrictSubPropertyOf",
		"DirectSubPropertyOf"
		// @formatter:on
	);

	public static void main(String[] args) throws IOException {
		for (String prefix : aBoxPrefixes) {
			for (AxiomType<?> axiomType : AxiomType.ABoxAxiomTypes) {
				generateJava(prefix + axiomType.getName());
			}
		}
		for (String prefix : allPrefixes) {
			for (AxiomType<?> axiomType : AxiomType.TBoxAxiomTypes) {
				generateJava(prefix + axiomType.getName());
			}
			for (AxiomType<?> axiomType : AxiomType.ABoxAxiomTypes) {
				generateJava(prefix + axiomType.getName());
			}
			for (AxiomType<?> axiomType : AxiomType.RBoxAxiomTypes) {
				generateJava(prefix + axiomType.getName());
			}
		}
		// for (String name : extra) {
		// String className = "Have" + name;
		// generateJava(className);
		// }
	}

	private static void generateJava(String className) throws IOException {
		File java = new File("output/" + className + ".java");
		java.createNewFile();
		PrintWriter pw = new PrintWriter(new FileWriter(java));
		printContent(pw, className);
		pw.flush();
		pw.close();
	}

	private static void printContent(PrintWriter writer, String className) {
		writer.println("package ru.agentlab.maia.annotation.ext;");
		writer.println();
		writer.println("import java.lang.annotation.Documented;");
		writer.println("import java.lang.annotation.ElementType;");
		writer.println("import java.lang.annotation.Retention;");
		writer.println("import java.lang.annotation.RetentionPolicy;");
		writer.println("import java.lang.annotation.Target;");
		writer.println();
//		writer.println("import ru.agentlab.maia.EventType;");
//		writer.println();
		writer.println("/**");
		writer.println(" * @author Dmitriy Shishkin");
		writer.println(" */");
		writer.println("@Documented");
		writer.println("@Retention(RetentionPolicy.RUNTIME)");
		writer.println("@Target(ElementType.METHOD)");
//		writer.println("@EventMatcher(EventType.BELIEF_CLASSIFICATION_ADDED)");
		writer.print("public @interface ");
		writer.print(className);
		writer.println(" {");
		writer.println();
		writer.println("	String value();");
		writer.println();
		writer.println("}");
	}

}
