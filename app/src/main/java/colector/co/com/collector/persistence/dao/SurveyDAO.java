package colector.co.com.collector.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import colector.co.com.collector.model.IdValue;
import colector.co.com.collector.model.Question;
import colector.co.com.collector.model.ResponseComplex;
import colector.co.com.collector.model.Section;
import colector.co.com.collector.model.Survey;
import colector.co.com.collector.persistence.DriverSQL;

public class SurveyDAO extends DriverSQL {

	private final static String TBL_NAME = "TBL_SURVEY";
	private final static String TBL_NAME_SECTION = "TBL_SECTION";
	private final static String TBL_NAME_QUESTION = "TBL_QUESTION";
	private final static String TBL_NAME_RESPONSE_COMPLEX = "TBL_RESPONSE_COMPLEX";
	private final static String TBL_NAME_RESPONSE_COMPLEX_OPTION = "TBL_RESPONSE_COMPLEX_OPTION";
	private final static String TBL_NAME_RESPONSE = "TBL_RESPONSE";

	private SQLiteDatabase db;

	public SurveyDAO(Context ctx) {
		super(ctx);
	}

	/**
	 * Insert or update surveys from login process
	 * 
	 * @param surveys list of surveys to insert
	 */
	public void synchronizeSurvey(List<Survey> surveys) {
		SQLiteDatabase db = getDBWrite();

		for (Survey survey:surveys){
			ContentValues initialValues = new ContentValues();
			initialValues.put("ID", survey.getForm_id());
			initialValues.put("NAME", survey.getForm_name());
			initialValues.put("DESCRIPTION", survey.getForm_description());

			// Inserta o actualiza un registro
			if ((int) db.insertWithOnConflict(TBL_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE) == -1) {
				db.update(TBL_NAME, initialValues, "ID=?", new String[]{String.valueOf(survey.getForm_id())});
                synchronizeSections(survey.getSections(), survey.getForm_id(), db);
			}
		}
		close();
	}

	/**
	 * Insert or update sections from login process
	 * @param sections list of sections to insert
	 * @param db db conection
	 */
	private void synchronizeSections(List<Section> sections, Long survey,SQLiteDatabase db){
		for (Section section: sections) {
			ContentValues initialValues = new ContentValues();
			initialValues.put("ID", section.getId());
			initialValues.put("NAME", section.getName());
			initialValues.put("DESCRIPTION", section.getDescription());
			initialValues.put("SURVEY", survey);

			// Inserta o actualiza un registro
			if ((int) db.insertWithOnConflict(TBL_NAME_SECTION, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE) == -1) {
				db.update(TBL_NAME_SECTION, initialValues, "ID=?", new String[]{String.valueOf(section.getId())});
                synchronizeQuestions(section.getInputs(), section.getId(), db);
			}
		}
	}

    /**
     * Insert or update questions from login process
     * @param questions list of questions to insert
     * @param db db conection
     */
    private void synchronizeQuestions(List<Question> questions, Long section,SQLiteDatabase db){
        for (Question question: questions) {

            ContentValues initialValues = new ContentValues();
            initialValues.put("ID", question.getId());
            initialValues.put("TYPE", question.getType());

            initialValues.put("NAME", question.getName());
            initialValues.put("DESCRIPTION", question.getDescription());
            initialValues.put("SECTION", section);

            // Inserta o actualiza un registro
            if ((int) db.insertWithOnConflict(TBL_NAME_QUESTION, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE) == -1) {
                db.update(TBL_NAME_QUESTION, initialValues, "ID=?", new String[]{String.valueOf(question.getId())});
                synchronizeResponses(question.getResponses(), question.getId(), db);
                synchronizeResponsesComplex(question.getFilled_forms(), question.getId(), db);
            }
        }
    }

    /**
     * Insert or update responses from login process
     * @param responses list of responses to insert
     * @param db db conection
     */
    private void synchronizeResponses(List<IdValue> responses, Long question,SQLiteDatabase db){
        for (IdValue response: responses) {

            ContentValues initialValues = new ContentValues();
            initialValues.put("ID", response.getId());
            initialValues.put("VALUE", response.getValue());
            initialValues.put("QUESTION", question);

            // Inserta o actualiza un registro
            if ((int) db.insertWithOnConflict(TBL_NAME_RESPONSE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE) == -1) {
                db.update(TBL_NAME_RESPONSE, initialValues, "ID=?", new String[]{String.valueOf(response.getId())});
            }
        }
    }

    /**
     * Insert or update responses from login process
     * @param responses list of responses to insert
     * @param db db conection
     */
    private void synchronizeResponsesComplex(List<ResponseComplex> responses, Long question,SQLiteDatabase db){
        for (ResponseComplex response: responses) {

            ContentValues initialValues = new ContentValues();
            initialValues.put("ID", response.getId());
            initialValues.put("VALUE", response.getValue());
            initialValues.put("QUESTION", question);

            // Inserta o actualiza un registro
            if ((int) db.insertWithOnConflict(TBL_NAME_RESPONSE_COMPLEX, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE) == -1) {
                db.update(TBL_NAME_RESPONSE_COMPLEX, initialValues, "ID=?", new String[]{String.valueOf(response.getId())});
                synchronizeResponsesComplexOptions(response.getData(),response.getId(),db);
            }
        }
    }

    /**
     * Insert or update responses from login process
     * @param responses list of responses to insert
     * @param db db conection
     */
    private void synchronizeResponsesComplexOptions(List<IdValue> responses, Long complex,SQLiteDatabase db){
        for (IdValue response: responses) {

            ContentValues initialValues = new ContentValues();
            initialValues.put("ID", response.getId());
            initialValues.put("VALUE", response.getValue());
            initialValues.put("COMPLEX", complex);

            // Inserta o actualiza un registro
            if ((int) db.insertWithOnConflict(TBL_NAME_RESPONSE_COMPLEX_OPTION, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE) == -1) {
                db.update(TBL_NAME_RESPONSE_COMPLEX_OPTION, initialValues, "ID=?", new String[]{String.valueOf(response.getId())});
            }
        }
    }


	/**
	 * Delete all surveys
	 */
	public void deleteAllSurveys() {
		deleteAll(TBL_NAME);
	}
	/**
	 * Delete all sections
	 */
	public void deleteAllSections() {
		deleteAll(TBL_NAME_SECTION);
	}

}
