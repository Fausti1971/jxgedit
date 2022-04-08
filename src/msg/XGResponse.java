package msg;

import javax.sound.midi.InvalidMidiDataException;

public interface XGResponse extends XGMessage
{
	/**
	 * returniert die Anzahl der Nutzdaten im BulkDump, bei XGMessageBulkDump aus dem Header, bei XGMessageParameterChange aus dem Opcode
	 * @return Länge des Nutzdatenblocks
	 */
	int getBulkSize();

	/**
	 * setzt die Anzahl der Nutzdaten des BulkDump im Header
	 * @param size
	 */
	void setBulkSize(int size);

	/**
	 * returniert das offset zu den Nutzdaten, das byte nach "address low"
	 * @return Offset zum Nutzdatenblock
	 */
	int getBaseOffset();

	/**
	 * errechnet die XG-Checksumme und wirft im Fehlerfall eine Exception
	 * @throws InvalidMidiDataException
	 */
	void checkSum() throws InvalidMidiDataException;

	/**
	 * errechnet, setzt und returniert die XG-Checksumme
	 */
	void setChecksum();
}
